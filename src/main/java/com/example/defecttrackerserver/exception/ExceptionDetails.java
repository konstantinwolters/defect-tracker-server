package com.example.defecttrackerserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;


/**
 * Custom exception details for API response.
 */
@AllArgsConstructor
@Getter
@Setter
public class ExceptionDetails {
    private final List<String> message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;
}
