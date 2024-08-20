package com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int errorCode;
    private String message;
}
