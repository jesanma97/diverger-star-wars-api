package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpecieDTO {
    @JsonProperty("average_height")
    private String averageHeight;
    @JsonProperty("average_lifespan")
    private String averageLifeSpan;
    private String classification;
    private String created;
    private String designation;
    private String edited;
    @JsonProperty("eye_colors")
    private String eyeColors;
    @JsonProperty("hair_colors")
    private String hairColors;
    private String homeworld;
    private String language;
    private String name;
    private List<String> people;
    private List<String> films;
    @JsonProperty("skin_colors")
    private String skinColors;
    private String url;
}
