package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VehicleDTO {
    @JsonProperty("cargo_capacity")
    private String cargoCapacity;
    private String consumables;
    @JsonProperty("cost_in_credits")
    private String costInCredits;
    private String created;
    private String crew;
    private String edited;
    private String length;
    private String manufacturer;
    @JsonProperty("max_atmosphering_speed")
    private String maxAtmospheringSpeed;
    private String model;
    private String name;
    private String passengers;
    private List<String> pilots;
    private List<String> films;
    private String url;
    @JsonProperty("vehicle_class")
    private String vehicleClass;
}
