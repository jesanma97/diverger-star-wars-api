package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlanetDTO {
    private String climate;
    private String created;
    private String diameter;
    private String edited;
    private List<String> films;
    private String gravity;
    private String name;
    @JsonProperty("orbital_period")
    private String orbitalPeriod;
    private String population;
    private List<String> residents;
    @JsonProperty("rotation_period")
    private String rotationPeriod;
    @JsonProperty("surface-water")
    private String surfaceWater;
    private String terrain;
    private String url;
}
