package com.diverger.RestAPIStarWars.infrastructure.adapters.out.persistence.mappers;

import com.diverger.RestAPIStarWars.domain.Film;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.FilmDTO;
import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.FilmResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FilmMapper {
    FilmMapper INSTANCE = Mappers.getMapper(FilmMapper.class);

    @Mapping(source = "title", target = "name")
    FilmResponseDTO filmToFilmResponseDTO(Film film);

    List<FilmResponseDTO> listFilmToListFilmResponseDTO(List<Film> filmList);
}
