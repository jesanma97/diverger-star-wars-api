package com.diverger.RestAPIStarWars.infrastructure.config;

import com.diverger.RestAPIStarWars.infrastructure.adapters.in.web.dto.ErrorResponse;
import com.diverger.RestAPIStarWars.infrastructure.commons.exceptions.BadRequestException;
import com.diverger.RestAPIStarWars.infrastructure.commons.exceptions.CharacterNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleCharacterNotFoundExceptionTest(){
        CharacterNotFoundException ex = new CharacterNotFoundException("Jesus");
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleCharacterNotFoundException(ex);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCode().value());
        assertEquals("Character with name Jesus not found", responseEntity.getBody().getMessage());
    }

    @Test
    public void handleInvalidHTTPMethodExceptionTest() {
        HttpRequestMethodNotSupportedException ex = mock(HttpRequestMethodNotSupportedException.class);
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleInvalidHTTPMethodException(ex);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), responseEntity.getStatusCode().value());
        assertEquals("Method not allowed to this request", responseEntity.getBody().getMessage());
    }

    @Test
    void handleBadRequestException(){
        BadRequestException ex = new BadRequestException("The character name must not be empty.");
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleBadRequestException(ex);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCode().value());
        assertEquals("The character name must not be empty.", responseEntity.getBody().getMessage());
    }
}
