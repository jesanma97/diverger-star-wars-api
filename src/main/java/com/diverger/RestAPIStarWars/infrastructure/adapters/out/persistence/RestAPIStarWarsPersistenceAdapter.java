package com.diverger.RestAPIStarWars.infrastructure.adapters.out.persistence;

import com.diverger.RestAPIStarWars.application.ports.out.RestAPIStarWarsPersistencePort;
import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import com.diverger.RestAPIStarWars.domain.FilmInfo;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.*;
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

    @Override
    @Cacheable(value = "characterCache", key = "#name")
    public Mono<CharacterResponse> getCharacterInfo(String name) {
        if (name == null || name.trim().isEmpty()) {
            LOGGER.error("Character name is null or empty");
            throw new BadRequestException("The character name must not be empty.");
        }

        return webClient.get()
                .uri("/people/?search={name}", name)
                .retrieve()
                .bodyToMono(ResultSearchCharacterDTO.class)
                .flatMap(dto -> {
                    return Mono.justOrEmpty(dto.getResults().stream()
                                    .filter(character -> character.getName().equalsIgnoreCase(name))
                                    .findFirst())
                            .switchIfEmpty(Mono.error(new CharacterNotFoundException(name))) // If it does not find a character in the filter, throw exception
                            .flatMap(this::buildCharacterInfo);
                })
                .doOnError(throwable -> LOGGER.error("Error occurred while fetching character info", throwable));
    }

    private Mono<CharacterResponse> buildCharacterInfo(CharacterDTO character) {
        LOGGER.info("Building character info for: {}", character.getName());
        Mono<String> planetName = webClient.get()
                .uri(character.getHomeWorld())
                .retrieve()
                .bodyToMono(PlanetDTO.class)
                .map(PlanetDTO::getName)
                .doOnError(throwable -> LOGGER.error("Error occurred while fetching planet info for {}", character.getHomeWorld(), throwable));

        Mono<String> fastestVehicleMono = getFastestVehicleUrl(character);

        Mono<List<FilmInfo>> filmsMono = Mono.just(character.getFilms())
                .flatMapMany(Flux::fromIterable)
                .flatMap(filmUrl -> webClient.get()
                        .uri(filmUrl)
                        .retrieve()
                        .bodyToMono(FilmDTO.class)
                        .map(filmDTO -> {
                            FilmInfo filmInfo = new FilmInfo();
                            filmInfo.setName(filmDTO.getTitle());
                            filmInfo.setReleaseDate(filmDTO.getReleaseDate());
                            return filmInfo;
                        })
                        .doOnError(throwable -> LOGGER.error("Error occurred while fetching film info for {}", filmUrl, throwable)))
                .collectList()
                .doOnError(throwable -> LOGGER.error("Error occurred while collecting film info", throwable));

        return Mono.zip(planetName, fastestVehicleMono, filmsMono)
                .map(tuple -> {
                    CharacterResponse info = new CharacterResponse();
                    info.setName(character.getName());
                    info.setBirthYear(character.getBirthYear());
                    info.setGender(character.getGender());
                    info.setPlanetName(tuple.getT1());
                    info.setFastestVehicleDriven(tuple.getT2());
                    info.setFilms(tuple.getT3());
                    return info;
                })
                .doOnError(throwable -> LOGGER.error("Error occurred while building character info", throwable));
    }

    protected Mono<String> getFastestVehicleUrl(CharacterDTO character) {
        LOGGER.info("Fetching fastest vehicle URL for character: {}", character.getName());

        List<String> allUrls = new ArrayList<>();
        allUrls.addAll(character.getVehicles());
        allUrls.addAll(character.getStarships());

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

    protected int parseMaxAtmospheringSpeed(String speed) {
        try {
            return Integer.parseInt(speed);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid max atmosphere speed value: {}", speed, e);
            return 0;
        }
    }
}