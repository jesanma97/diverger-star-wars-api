package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.controllers;

import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import com.diverger.RestAPIStarWars.domain.FilmInfo;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.RestAPIStarWarsConsultAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestAPIStarWarsControllerTest {

    @Mock
    private RestAPIStarWarsConsultAdapter restAPIStarWarsConsultAdapter;

    @InjectMocks
    private RestAPIStarWarsController restAPIStarWarsController;

    Mono<CharacterResponse> characterResponseMono;

    @BeforeEach
    public void setUp(){
        this.restAPIStarWarsController = new RestAPIStarWarsController(restAPIStarWarsConsultAdapter);
        CharacterResponse characterResponse = new CharacterResponse();
        characterResponse.setName("Luke Skywalker");
        characterResponse.setBirthYear("19BBY");
        characterResponse.setGender("male");
        characterResponse.setPlanetName("Tatooine");
        characterResponse.setFastestVehicleDriven("X-wing");
        characterResponse.setFilms(List.of(new FilmInfo("A New Hope", new Date()), new FilmInfo("The Empire Strikes Back", new Date())));

        characterResponseMono = Mono.just(characterResponse);
    }
    @Test
    void getCharacterInfo(){
        when(this.restAPIStarWarsConsultAdapter.getCharacterInfo(Mockito.anyString())).thenReturn(characterResponseMono);
        Mono<CharacterResponse> responseMono = restAPIStarWarsController.getCharacterInfo("Luke Skywalker");

        StepVerifier.create(responseMono)
                .expectNextMatches(characterResponse -> {
                    return characterResponse.getName().equals("Luke Skywalker") &&
                            characterResponse.getBirthYear().equals("19BBY") &&
                            characterResponse.getGender().equals("male") &&
                            characterResponse.getPlanetName().equals("Tatooine") &&
                            characterResponse.getFastestVehicleDriven().equals("X-wing") &&
                            characterResponse.getFilms().size() == 2 &&
                            characterResponse.getFilms().get(0).getName().equals("A New Hope") &&
                            characterResponse.getFilms().get(1).getName().equals("The Empire Strikes Back");
                })
                .expectComplete()
                .verify();

    }


}
