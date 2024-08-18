package com.diverger.RestAPIStarWars.infrastructure.adapters.out.persistence;

import com.diverger.RestAPIStarWars.application.ports.out.RestAPIStarWarsPersistencePort;
import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import com.diverger.RestAPIStarWars.domain.FilmInfo;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.*;
import com.diverger.RestAPIStarWars.infrastructure.commons.exceptions.CharacterNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class RestAPIStarWarsPersistenceAdapter implements RestAPIStarWarsPersistencePort {

    private final WebClient webClient;

    @Autowired
    public RestAPIStarWarsPersistenceAdapter(WebClient webClient){
        this.webClient = webClient;
    }
    @Override
    public Mono<CharacterResponse> getCharacterInfo(String name) {
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
                });
    }

    private Mono<CharacterResponse> buildCharacterInfo(CharacterDTO character) {
        Mono<String> planetName =  webClient.get()
                .uri(character.getHomeWorld())
                .retrieve()
                .bodyToMono(PlanetDTO.class) // Map the response to PlanetDTO
                .map(PlanetDTO::getName);

        Mono<String> fastestVehicleMono = getFastestVehicleUrl(character);

        Mono<List<FilmInfo>> filmsMono = Mono.just(character.getFilms())
                .flatMapMany(Flux::fromIterable)
                .flatMap(filmUrl -> webClient.get()
                        .uri(filmUrl)
                        .retrieve()
                        .bodyToMono(FilmDTO.class) // Get FilmDTO
                        .map(filmDTO -> {
                            FilmInfo filmInfo = new FilmInfo();
                            filmInfo.setName(filmDTO.getTitle()); // Map title to name
                            filmInfo.setReleaseDate(filmDTO.getReleaseDate()); // Map release_date
                            return filmInfo;
                        }))
                .collectList();

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
                });
    }

    private Mono<String> getFastestVehicleUrl(CharacterDTO character) {
        // Create a flow of URLs for vehicles and spaceships
        List<String> allUrls = new ArrayList<>();
        allUrls.addAll(character.getVehicles());
        allUrls.addAll(character.getStarships());

        // If there are no URLs, return null
        if (allUrls.isEmpty()) {
            return Mono.justOrEmpty(null);
        }

        // Create a Mono flow to get all the details of vehicles and spaceships
        return Flux.fromIterable(allUrls)
                .flatMap(url -> {
                    if (url.contains("vehicles")) {
                        return webClient.get().uri(url).retrieve().bodyToMono(VehicleDTO.class)
                                .map(vehicle -> new AbstractMap.SimpleEntry<>(vehicle.getName(), vehicle.getMaxAtmospheringSpeed()));
                    } else if (url.contains("starships")) {
                        return webClient.get().uri(url).retrieve().bodyToMono(StarshipDTO.class)
                                .map(starship -> new AbstractMap.SimpleEntry<>(starship.getName(), starship.getMaxAtmospheringSpeed()));
                    } else {
                        return Mono.empty();
                    }
                })
                .collectList()
                .map(entries -> entries.stream()
                        .max(Comparator.comparing(entry -> parseMaxAtmospheringSpeed(entry.getValue())))
                        .map(Map.Entry::getKey)
                        .orElse(null));
    }

    private int parseMaxAtmospheringSpeed(String speed) {
        try {
            return Integer.parseInt(speed);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
