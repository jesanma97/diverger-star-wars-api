package com.diverger.RestAPIStarWars.application.ports.in;

import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.CharacterResponseDTO;
import reactor.core.publisher.Flux;

public interface RestAPIStarWarsConsultPort {
    Flux<CharacterResponseDTO> getCharacterInfo(String name);
}
