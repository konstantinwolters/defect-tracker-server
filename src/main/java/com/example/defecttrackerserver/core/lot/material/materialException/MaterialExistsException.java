package com.example.defecttrackerserver.core.lot.material.materialException;

import lombok.Getter;

@Getter
public class MaterialExistsException extends RuntimeException{
    public MaterialExistsException(String message) {
        super(message);
    }

    public MaterialExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
