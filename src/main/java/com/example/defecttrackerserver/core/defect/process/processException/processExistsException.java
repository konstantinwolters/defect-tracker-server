package com.example.defecttrackerserver.core.defect.process.processException;

import lombok.Getter;

@Getter
public class processExistsException extends RuntimeException{
    public processExistsException(String message) {
        super(message);
    }

    public processExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
