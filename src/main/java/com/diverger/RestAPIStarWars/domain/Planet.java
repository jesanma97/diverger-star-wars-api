package com.diverger.RestAPIStarWars.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Planet {
    private String climate;
    private String created;
    private String diameter;
    private String edited;
    private List<Film> films;
    private String gravity;
    private String name;
    private String orbitalPeriod;
    private String population;
    private List<CharacterDomain> residents;
    private String rotationPeriod;
    private String surfaceWater;
    private String terrain;
    private String url;
}
