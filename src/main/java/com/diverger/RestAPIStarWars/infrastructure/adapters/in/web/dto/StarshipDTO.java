package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StarshipDTO {
    private String mglt;  // MGLT stands for "Megalight per hour"
    @JsonProperty("cargo_capacity")
    private String cargoCapacity;
    private String consumables;
    @JsonProperty("cost_in_credits")
    private String costInCredits;
    private String created;
    private String crew;
    private String edited;
    @JsonProperty("hyperdrive_rating")
    private String hyperDriveRating;
    private String length;
    private String manufacturer;
    @JsonProperty("max_atmosphering_speed")
    private String maxAtmospheringSpeed;
    private String model;
    private String name;
    private String passengers;
    private List<String> films;
    private List<String> pilots;
    @JsonProperty("starship_class")
    private String starshipClass;
    private String url;
}
