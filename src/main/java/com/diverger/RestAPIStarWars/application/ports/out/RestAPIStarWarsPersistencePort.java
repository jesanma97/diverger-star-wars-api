package com.diverger.RestAPIStarWars.application.ports.out;

import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import reactor.core.publisher.Flux;

public interface RestAPIStarWarsPersistencePort {
    Flux<CharacterResponse> getCharacterInfo(String name);
}
