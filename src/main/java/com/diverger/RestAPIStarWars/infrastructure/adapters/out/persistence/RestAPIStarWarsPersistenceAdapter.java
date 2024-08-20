package com.diverger.RestAPIStarWars.infrastructure.adapters.out.persistence;

import com.diverger.RestAPIStarWars.application.ports.out.RestAPIStarWarsPersistencePort;
import com.diverger.RestAPIStarWars.domain.CharacterDomain;
import com.diverger.RestAPIStarWars.domain.Film;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.*;
import com.diverger.RestAPIStarWars.infrastructure.adapters.out.persistence.mappers.CharacterDomainMapper;
import com.diverger.RestAPIStarWars.infrastructure.adapters.out.persistence.mappers.FilmMapper;
import com.diverger.RestAPIStarWars.infrastructure.commons.exceptions.BadRequestException;
import com.diverger.RestAPIStarWars.infrastructure.commons.exceptions.CharacterNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Component
public class RestAPIStarWarsPersistenceAdapter implements RestAPIStarWarsPersistencePort {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAPIStarWarsPersistenceAdapter.class);

    private final WebClient webClient;

    @Autowired
    public RestAPIStarWarsPersistenceAdapter(WebClient webClient){
        this.webClient = webClient;
    }

    /**
     * Fetches character information based on the provided name.
     * The method queries an external API, processes the data, and returns a Flux of CharacterResponseDTO.
     * If the character name is invalid or empty, it throws a BadRequestException.
     * If the result is empty, it throws a CharacterNotFoundException
     *
     * @param name The name of the character to search for.
     * @return Flux of CharacterResponseDTO containing the character details.
     * @throws BadRequestException if the character name is null or empty.
     * @throws CharacterNotFoundException if the character is not found in the external API
     */
    @Override
    @Cacheable(value = "characterCache", key = "#name")
    public Flux<CharacterResponseDTO> getCharacterInfo(String name) {
        if (name == null || name.trim().isEmpty()) {
            LOGGER.error("Character name is null or empty");
            throw new BadRequestException("The character name must not be empty.");
        }

        return webClient.get()
                .uri("/people/?search={name}", name)
                .retrieve()
                .bodyToMono(ResultSearchCharacterDTO.class)
                .flatMapMany(dto -> Flux.fromIterable(dto.getResults())
                        .flatMap(this::buildCharacterDomain) // Convert CharacterDTO to Character domain object
                        .map(this::toCharacterResponseDTO) // Convert Character domain object to CharacterResponseDTO
                        .switchIfEmpty(Flux.error(new CharacterNotFoundException(name))))
                .doOnError(throwable -> LOGGER.error("Error occurred while fetching character info", throwable));
    }

    /**
     * Builds a Character domain object from a CharacterDTO received from the external API.
     * It fetches additional data like the character's home planet, fastest vehicle, and films.
     *
     * @param characterDTO The CharacterDTO received from the external API.
     * @return Mono of Character containing all the domain information of the character.
     */
    private Mono<CharacterDomain> buildCharacterDomain(CharacterDTO characterDTO) {
        LOGGER.info("Building character domain object for: {}", characterDTO.getName());

        Mono<String> planetName = webClient.get()
                .uri(characterDTO.getHomeWorld())
                .retrieve()
                .bodyToMono(PlanetDTO.class)
                .map(PlanetDTO::getName)
                .doOnError(throwable -> LOGGER.error("Error occurred while fetching planet info for {}", characterDTO.getHomeWorld(), throwable));

        Mono<String> fastestVehicleMono = getFastestVehicleUrl(characterDTO);

        Mono<List<Film>> filmsMono = Mono.just(characterDTO.getFilms())
                .flatMapMany(Flux::fromIterable)
                .flatMap(filmUrl -> webClient.get()
                        .uri(filmUrl)
                        .retrieve()
                        .bodyToMono(FilmDTO.class)
                        .map(this::convertToFilm)
                        .doOnError(throwable -> LOGGER.error("Error occurred while fetching film info for {}", filmUrl, throwable)))
                .collectList()
                .doOnError(throwable -> LOGGER.error("Error occurred while collecting film info", throwable));

        return Mono.zip(planetName, fastestVehicleMono, filmsMono)
                .map(tuple -> new CharacterDomain(
                        characterDTO.getName(),
                        characterDTO.getBirthYear(),
                        characterDTO.getGender(),
                        tuple.getT1(),
                        tuple.getT2(),
                        tuple.getT3()
                ))
                .doOnError(throwable -> LOGGER.error("Error occurred while building character domain object", throwable));
    }

    /**
     * Converts a Character domain object to a CharacterResponseDTO.
     * This method is used to prepare the data for sending back to the client.
     *
     * @param character The Character domain object containing all the relevant information.
     * @return CharacterResponseDTO with the character's information formatted for the API response.
     */
    private CharacterResponseDTO toCharacterResponseDTO(CharacterDomain character) {
        return CharacterDomainMapper.INSTANCE.characterDomainToCharacterResponse(character);
    }

    /**
     * Converts a FilmDTO to a domain object Film.
     * This method is used to prepare the data for the domain layer
     *
     * @param filmDTO The Film DTO containing all the relevant information.
     * @return Film with the film's information received in the external API.
     */
    private Film convertToFilm(FilmDTO filmDTO) {
        return new Film(filmDTO.getTitle(), filmDTO.getReleaseDate());
    }

    /**
     * Fetches the fastest vehicle URL for the given character from the external API.
     * It checks both vehicles and starships, and returns the name of the fastest one.
     *
     * @param characterDTO The CharacterDTO containing the URLs for vehicles and starships.
     * @return Mono of String containing the name of the fastest vehicle or "n/a" if no vehicles are found.
     */
    protected Mono<String> getFastestVehicleUrl(CharacterDTO characterDTO) {
        LOGGER.info("Fetching fastest vehicle URL for character: {}", characterDTO.getName());

        List<String> allUrls = new ArrayList<>();
        allUrls.addAll(characterDTO.getVehicles());
        allUrls.addAll(characterDTO.getStarships());

        if (allUrls.isEmpty()) {
            return Mono.justOrEmpty("n/a");
        }

        return Flux.fromIterable(allUrls)
                .flatMap(url -> {
                    if (url.contains("vehicles")) {
                        return webClient.get().uri(url).retrieve().bodyToMono(VehicleDTO.class)
                                .map(vehicle -> new AbstractMap.SimpleEntry<>(vehicle.getName(), vehicle.getMaxAtmospheringSpeed()))
                                .doOnError(throwable -> LOGGER.error("Error occurred while fetching vehicle info from URL {}", url, throwable));
                    } else if (url.contains("starships")) {
                        return webClient.get().uri(url).retrieve().bodyToMono(StarshipDTO.class)
                                .map(starship -> new AbstractMap.SimpleEntry<>(starship.getName(), starship.getMaxAtmospheringSpeed()))
                                .doOnError(throwable -> LOGGER.error("Error occurred while fetching starship info from URL {}", url, throwable));
                    } else {
                        return Mono.empty();
                    }
                })
                .collectList()
                .map(entries -> entries.stream()
                        .max(Comparator.comparing(entry -> parseMaxAtmospheringSpeed(entry.getValue())))
                        .map(Map.Entry::getKey)
                        .orElse(null))
                .doOnError(throwable -> LOGGER.error("Error occurred while collecting vehicle and starship info", throwable));
    }

    /**
     * Parses a string representing the maximum atmospheric speed and converts it to an integer.
     * If the value is invalid, it returns 0.
     *
     * @param speed The string value representing the speed.
     * @return The parsed speed as an integer, or 0 if the input is invalid.
     */
    protected int parseMaxAtmospheringSpeed(String speed) {
        try {
            return Integer.parseInt(speed);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid max atmosphere speed value: {}", speed, e);
            return 0;
        }
    }
}