package com.diverger.RestAPIStarWars.application.ports.in;

import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import reactor.core.publisher.Mono;

public interface RestAPIStarWarsConsultPort {
    Mono<CharacterResponse> getCharacterInfo(String name);
}
