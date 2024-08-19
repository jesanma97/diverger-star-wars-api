package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web;

import com.diverger.RestAPIStarWars.application.services.RestAPIStarWarsService;
import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import com.diverger.RestAPIStarWars.domain.FilmInfo;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.controllers.RestAPIStarWarsController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestAPIStarWarsConsultAdapterTest {
    @Mock
    private RestAPIStarWarsService restAPIStarWarsService;

    @InjectMocks
    private RestAPIStarWarsConsultAdapter restAPIStarWarsConsultAdapter;

    Flux<CharacterResponse> characterResponseFlux;

    @BeforeEach
    public void setUp() {
        this.restAPIStarWarsConsultAdapter = new RestAPIStarWarsConsultAdapter(restAPIStarWarsService);

        // Configuración de CharacterResponse para las pruebas
        CharacterResponse characterResponse1 = new CharacterResponse();
        characterResponse1.setName("Luke Skywalker");
        characterResponse1.setBirthYear("19BBY");
        characterResponse1.setGender("male");
        characterResponse1.setPlanetName("Tatooine");
        characterResponse1.setFastestVehicleDriven("X-wing");
        characterResponse1.setFilms(List.of(
                new FilmInfo("A New Hope", new Date()),
                new FilmInfo("The Empire Strikes Back", new Date())
        ));

        CharacterResponse characterResponse2 = new CharacterResponse();
        characterResponse2.setName("Leia Organa");
        characterResponse2.setBirthYear("19BBY");
        characterResponse2.setGender("female");
        characterResponse2.setPlanetName("Alderaan");
        characterResponse2.setFastestVehicleDriven("Speeder Bike");
        characterResponse2.setFilms(List.of(
                new FilmInfo("A New Hope", new Date()),
                new FilmInfo("Return of the Jedi", new Date())
        ));

        // Convertir los CharacterResponse en un Flux
        characterResponseFlux = Flux.just(characterResponse1, characterResponse2);
    }

    @Test
    void getCharacterInfo() {
        when(this.restAPIStarWarsService.getCharacterInfo(Mockito.anyString())).thenReturn(characterResponseFlux);

        Flux<CharacterResponse> responseFlux = restAPIStarWarsConsultAdapter.getCharacterInfo("Skywalker");

        StepVerifier.create(responseFlux)
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
                .expectNextMatches(characterResponse -> {
                    return characterResponse.getName().equals("Leia Organa") &&
                            characterResponse.getBirthYear().equals("19BBY") &&
                            characterResponse.getGender().equals("female") &&
                            characterResponse.getPlanetName().equals("Alderaan") &&
                            characterResponse.getFastestVehicleDriven().equals("Speeder Bike") &&
                            characterResponse.getFilms().size() == 2 &&
                            characterResponse.getFilms().get(0).getName().equals("A New Hope") &&
                            characterResponse.getFilms().get(1).getName().equals("Return of the Jedi");
                })
                .expectComplete()
                .verify();
    }
}
