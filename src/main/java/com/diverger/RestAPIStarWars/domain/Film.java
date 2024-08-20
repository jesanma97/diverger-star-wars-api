package com.diverger.RestAPIStarWars.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private List<CharacterDomain> characters;
    private String created;
    private String director;
    private String edited;
    @JsonProperty("episode_id")
    private int episodeId;
    @JsonProperty("opening_crawl")
    private String openingCrawl;
    private List<Planet> planets;
    private String producer;
    @JsonProperty("release_date")
    private Date releaseDate;
    private List<Specie> species;
    private List<Starship> starships;
    private String title;
    private String url;
    private List<Vehicle> vehicles;

    public Film(String title, Date releaseDate){
        this.title = title;
        this.releaseDate = releaseDate;
    }
}
