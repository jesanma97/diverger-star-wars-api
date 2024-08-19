package com.diverger.RestAPIStarWars.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({"name", "birth_year", "gender", "planet_name", "fastest_vehicle_driven", "films"})
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
