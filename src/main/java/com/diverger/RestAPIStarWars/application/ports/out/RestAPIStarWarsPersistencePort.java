package com.diverger.RestAPIStarWars.application.ports.out;

import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import reactor.core.publisher.Mono;

public interface RestAPIStarWarsPersistencePort {
    Mono<CharacterResponse> getCharacterInfo(String name);
}
