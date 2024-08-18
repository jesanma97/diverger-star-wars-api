package com.diverger.RestAPIStarWars.infrastructure.adapters.out.persistence;

import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.*;
import com.diverger.RestAPIStarWars.infrastructure.commons.exceptions.BadRequestException;
import com.diverger.RestAPIStarWars.infrastructure.commons.exceptions.CharacterNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RestAPIStarWarsPersistenceAdapterTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private RestAPIStarWarsPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object[].class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void getCharacterInfoSuccess() {
        // Mock response for character search
        ResultSearchCharacterDTO resultSearchCharacterDTO = new ResultSearchCharacterDTO();
        CharacterDTO characterDTO = new CharacterDTO();
        characterDTO.setName("Luke Skywalker");
        characterDTO.setHomeWorld("https://swapi.trileuco.com/api/planets/1/");
        resultSearchCharacterDTO.setResults(List.of(characterDTO));

        // Mock response for planet, vehicles, films and starships
        PlanetDTO planetDTO = new PlanetDTO();
        planetDTO.setName("Tatooine");

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setName("X-34 landspeeder");
        vehicleDTO.setMaxAtmospheringSpeed("250");

        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setTitle("A New Hope");
        filmDTO.setReleaseDate(new Date());

        StarshipDTO starshipDTO = new StarshipDTO();
        starshipDTO.setName("X-34 landspeeder");
        starshipDTO.setMaxAtmospheringSpeed("250");

        characterDTO.setVehicles(List.of("https://swapi.trileuco.com/api/vehicles/1/"));
        characterDTO.setStarships(List.of("https://swapi.trileuco.com/api/starships/1/"));
        characterDTO.setFilms(List.of("https://swapi.trileuco.com/api/films/1/"));


        when(responseSpec.bodyToMono(ResultSearchCharacterDTO.class)).thenReturn(Mono.just(resultSearchCharacterDTO));
        when(responseSpec.bodyToMono(PlanetDTO.class)).thenReturn(Mono.just(planetDTO));
        when(responseSpec.bodyToMono(VehicleDTO.class)).thenReturn(Mono.just(vehicleDTO));
        when(responseSpec.bodyToMono(FilmDTO.class)).thenReturn(Mono.just(filmDTO));
        when(responseSpec.bodyToMono(StarshipDTO.class)).thenReturn(Mono.just(starshipDTO));

        // Execute the method
        Mono<CharacterResponse> result = adapter.getCharacterInfo("Luke Skywalker");

        // Verify and assert the result
        StepVerifier.create(result)
                .expectNextMatches(characterResponse -> characterResponse.getName().equals("Luke Skywalker")
                        && characterResponse.getPlanetName().equals("Tatooine")
                        && characterResponse.getFastestVehicleDriven().equals("X-34 landspeeder")
                        && characterResponse.getFilms().size() == 1
                        && characterResponse.getFilms().get(0).getName().equals("A New Hope"))
                .verifyComplete();
    }

    @Test
    void getCharacterInfoCharacterNotFound() {
        ResultSearchCharacterDTO resultSearchCharacterDTO = new ResultSearchCharacterDTO();
        resultSearchCharacterDTO.setResults(List.of());

        when(responseSpec.bodyToMono(ResultSearchCharacterDTO.class)).thenReturn(Mono.just(resultSearchCharacterDTO));

        Mono<CharacterResponse> result = adapter.getCharacterInfo("Unknown Character");

        StepVerifier.create(result)
                .expectError(CharacterNotFoundException.class)
                .verify();
    }

    @Test
    void getCharacterInfoDirectExceptionTest() {
        try {
            adapter.getCharacterInfo("").block(); // Force Mono Evaluation
            fail("Expected BadRequestException not thrown.");
        } catch (BadRequestException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("The character name must not be empty.", ex.getReason());
        }
    }
    @Test
    void getCharacterInfoServerError() {
        when(responseSpec.bodyToMono(ResultSearchCharacterDTO.class)).thenReturn(Mono.error(WebClientResponseException.InternalServerError.create(500, "Internal Server Error", null, null, null)));

        Mono<CharacterResponse> result = adapter.getCharacterInfo("Luke Skywalker");

        StepVerifier.create(result)
                .expectError(WebClientResponseException.InternalServerError.class)
                .verify();
    }
}