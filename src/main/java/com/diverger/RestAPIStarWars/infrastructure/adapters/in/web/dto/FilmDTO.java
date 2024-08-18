package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FilmDTO {
    private List<String> characters;
    private String created;
    private String director;
    private String edited;
    @JsonProperty("episode_id")
    private int episodeId;
    @JsonProperty("opening_crawl")
    private String openingCrawl;
    private List<String> planets;
    private String producer;
    @JsonProperty("release_date")
    private Date releaseDate;
    private List<String> species;
    private List<String> starships;
    private String title;
    private String url;
    private List<String> vehicles;
}
