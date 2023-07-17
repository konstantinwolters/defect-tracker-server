package com.example.defecttrackerserver.core.location.locationException;

import lombok.Getter;

@Getter
public class LocationExistsException extends RuntimeException{
    public LocationExistsException(String message) {
        super(message);
    }

    public LocationExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
