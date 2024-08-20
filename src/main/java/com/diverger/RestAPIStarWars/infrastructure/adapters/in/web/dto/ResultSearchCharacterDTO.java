package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResultSearchCharacterDTO {
    private int count;
    private String next;
    private String previous;
    private List<CharacterDTO> results;
}
