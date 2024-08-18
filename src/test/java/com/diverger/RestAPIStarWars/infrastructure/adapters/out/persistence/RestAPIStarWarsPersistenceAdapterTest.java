package com.diverger.RestAPIStarWars.infrastructure.adapters.out.persistence;

import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import com.diverger.RestAPIStarWars.domain.FilmInfo;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.CharacterDTO;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.PlanetDTO;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.ResultSearchCharacterDTO;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.VehicleDTO;
import com.diverger.RestAPIStarWars.infrastructure.commons.exceptions.BadRequestException;
import com.diverger.RestAPIStarWars.infrastructure.commons.exceptions.CharacterNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class RestAPIStarWarsPersistenceAdapterTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private RestAPIStarWarsPersistenceAdapter adapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mocking the WebClient behavior
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void testGetCharacterInfoSuccess() {
        // Create mock data
        CharacterDTO characterDTO = new CharacterDTO();
        characterDTO.setName("Luke Skywalker");
        characterDTO.setBirthYear("19BBY");
        characterDTO.setGender("male");
        characterDTO.setHomeWorld("/planets/1/");
        characterDTO.setVehicles(List.of("/vehicles/14/"));
        characterDTO.setStarships(new ArrayList<>()); // Assuming no starships

        ResultSearchCharacterDTO searchResult = new ResultSearchCharacterDTO();
        searchResult.setResults(List.of(characterDTO));

        PlanetDTO planetDTO = new PlanetDTO();
        planetDTO.setName("Tatooine");

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setName("X-wing");
        vehicleDTO.setMaxAtmospheringSpeed("1050");

        // Set up mocks for different endpoints
        when(responseSpec.bodyToMono(ResultSearchCharacterDTO.class)).thenReturn(Mono.just(searchResult));
        when(webClient.get().uri("/planets/1/").retrieve().bodyToMono(PlanetDTO.class)).thenReturn(Mono.just(planetDTO));
        when(webClient.get().uri("/vehicles/14/").retrieve().bodyToMono(VehicleDTO.class)).thenReturn(Mono.just(vehicleDTO));

        Mono<CharacterResponse> responseMono = adapter.getCharacterInfo("Luke Skywalker");

        StepVerifier.create(responseMono)
                .expectNextMatches(characterResponse ->
                        "Luke Skywalker".equals(characterResponse.getName()) &&
                                "19BBY".equals(characterResponse.getBirthYear()) &&
                                "male".equals(characterResponse.getGender()) &&
                                "Tatooine".equals(characterResponse.getPlanetName()) &&
                                "X-wing".equals(characterResponse.getFastestVehicleDriven()) &&
                                characterResponse.getFilms().isEmpty() // No films in mock data
                )
                .expectComplete()
                .verify();
    }

    @Test
    void testGetCharacterInfoWithInvalidName() {
        // Test for null or empty character name
        Mono<CharacterResponse> responseMono = adapter.getCharacterInfo(null);

        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable -> throwable instanceof BadRequestException &&
                        throwable.getMessage().equals("The character name must not be empty."))
                .verify();
    }

    @Test
    void testGetCharacterInfoNotFound() {
        // Mock response when character is not found
        ResultSearchCharacterDTO searchResult = new ResultSearchCharacterDTO();
        searchResult.setResults(new ArrayList<>()); // No characters found

        when(responseSpec.bodyToMono(ResultSearchCharacterDTO.class)).thenReturn(Mono.just(searchResult));

        Mono<CharacterResponse> responseMono = adapter.getCharacterInfo("Unknown");

        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable -> throwable instanceof CharacterNotFoundException &&
                        throwable.getMessage().equals("Unknown"))
                .verify();
    }
}