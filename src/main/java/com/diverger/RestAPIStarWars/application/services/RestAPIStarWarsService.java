package com.diverger.RestAPIStarWars.application.services;

import com.diverger.RestAPIStarWars.application.ports.out.RestAPIStarWarsPersistencePort;
import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import com.diverger.RestAPIStarWars.infrastructure.adapters.out.persistence.RestAPIStarWarsPersistenceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RestAPIStarWarsService {

    private final RestAPIStarWarsPersistencePort restAPIStarWarsPersistencePort;

    @Autowired
    public RestAPIStarWarsService(RestAPIStarWarsPersistenceAdapter restAPIStarWarsPersistenceAdapter){
        this.restAPIStarWarsPersistencePort = restAPIStarWarsPersistenceAdapter;
    }

    public Mono<CharacterResponse> getCharacterInfo(String name) {
        return restAPIStarWarsPersistencePort.getCharacterInfo(name);
    }
}
