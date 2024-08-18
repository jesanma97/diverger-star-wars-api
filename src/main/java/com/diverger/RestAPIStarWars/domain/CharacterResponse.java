package com.diverger.RestAPIStarWars.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CharacterResponse {
    private String name;
    @JsonProperty("birth_year")
    private String birthYear;
    private String gender;
    @JsonProperty("planet_name")
    private String planetName;
    @JsonProperty("fastest_vehicle_driven")
    private String fastestVehicleDriven;
    private List<FilmInfo> films;
}
