package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto;

import com.diverger.RestAPIStarWars.domain.Film;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"name", "birth_year", "gender", "planet_name", "fastest_vehicle_driven", "films"})
public class CharacterResponseDTO {
    private String name;
    @JsonProperty("birth_year")
    private String birthYear;
    private String gender;
    @JsonProperty("planet_name")
    private String planetName;
    @JsonProperty("fastest_vehicle_driven")
    private String fastestVehicleDriven;
    private List<FilmResponseDTO> films;
}
