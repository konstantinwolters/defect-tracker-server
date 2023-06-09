package com.example.defecttrackerserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ExceptionDetails {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;
}
