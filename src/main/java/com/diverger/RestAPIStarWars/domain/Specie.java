package com.diverger.RestAPIStarWars.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Specie {
    private String averageHeight;
    private String averageLifeSpan;
    private String classification;
    private String created;
    private String designation;
    private String edited;
    private String eyeColors;
    private String hairColors;
    private Planet homeworld;
    private String language;
    private String name;
    private List<CharacterDomain> people;
    private List<Film> films;
    private String skinColors;
    private String url;
}
