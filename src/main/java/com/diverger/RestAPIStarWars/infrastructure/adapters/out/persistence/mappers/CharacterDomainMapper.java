package com.diverger.RestAPIStarWars.infrastructure.adapters.out.persistence.mappers;

import com.diverger.RestAPIStarWars.domain.CharacterDomain;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.CharacterResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = FilmMapper.class)
public interface CharacterDomainMapper {
    CharacterDomainMapper INSTANCE = Mappers.getMapper(CharacterDomainMapper.class);

    CharacterResponseDTO characterDomainToCharacterResponse(CharacterDomain characterDomain);

}
