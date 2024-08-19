package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web;

import com.diverger.RestAPIStarWars.application.ports.in.RestAPIStarWarsConsultPort;
import com.diverger.RestAPIStarWars.application.services.RestAPIStarWarsService;
import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class RestAPIStarWarsConsultAdapter implements RestAPIStarWarsConsultPort {

    private final RestAPIStarWarsService restAPIStarWarsService;

    @Autowired
    public RestAPIStarWarsConsultAdapter(RestAPIStarWarsService restAPIStarWarsService){
        this.restAPIStarWarsService = restAPIStarWarsService;
    }

    @Override
    public Flux<CharacterResponse> getCharacterInfo(String name) {
        return restAPIStarWarsService.getCharacterInfo(name);
    }
}
