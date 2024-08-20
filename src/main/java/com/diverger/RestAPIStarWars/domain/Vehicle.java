package com.diverger.RestAPIStarWars.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Vehicle {
    private String cargoCapacity;
    private String consumables;
    private String costInCredits;
    private String created;
    private String crew;
    private String edited;
    private String length;
    private String manufacturer;
    private String maxAtmospheringSpeed;
    private String model;
    private String name;
    private String passengers;
    private List<CharacterDomain> pilots;
    private List<Film> films;
    private String url;
    private String vehicleClass;
}
