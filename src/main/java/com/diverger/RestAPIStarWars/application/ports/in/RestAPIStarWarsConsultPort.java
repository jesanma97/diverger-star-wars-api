package com.diverger.RestAPIStarWars.application.ports.in;

import com.diverger.RestAPIStarWars.domain.CharacterResponse;
import reactor.core.publisher.Flux;

public interface RestAPIStarWarsConsultPort {
    Flux<CharacterResponse> getCharacterInfo(String name);
}
