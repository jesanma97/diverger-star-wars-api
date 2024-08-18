package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.controllers;

import com.diverger.RestAPIStarWars.application.ports.in.RestAPIStarWarsConsultPort;
import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.RestAPIStarWarsConsultAdapter;
import com.diverger.RestAPIStarWars.infrastructure.commons.Endpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(Endpoints.PARENT_URL)
public class RestAPIStarWarsController {

    private final RestAPIStarWarsConsultPort restAPIStarWarsConsultPort;

    @Autowired
    public RestAPIStarWarsController(RestAPIStarWarsConsultAdapter restAPIStarWarsConsultAdapter){
        this.restAPIStarWarsConsultPort = restAPIStarWarsConsultAdapter;
    }

    @GetMapping(Endpoints.PERSON_INFO)
    public Mono<CharacterResponse> getCharacterInfo(@RequestParam String name){
        return restAPIStarWarsConsultPort.getCharacterInfo(name);
    }
}
