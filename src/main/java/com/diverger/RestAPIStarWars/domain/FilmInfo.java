package com.diverger.RestAPIStarWars.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FilmInfo {
    private String name;
    @JsonProperty("release_date")
    private Date releaseDate;
}
