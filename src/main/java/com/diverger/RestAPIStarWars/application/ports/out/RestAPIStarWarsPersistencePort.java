package com.diverger.RestAPIStarWars.application.ports.out;

import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.CharacterResponseDTO;
import reactor.core.publisher.Flux;

public interface RestAPIStarWarsPersistencePort {
    Flux<CharacterResponseDTO> getCharacterInfo(String name);
}
