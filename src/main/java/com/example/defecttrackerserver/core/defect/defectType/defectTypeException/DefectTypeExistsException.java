package com.example.defecttrackerserver.core.defect.defectType.defectTypeException;

import lombok.Getter;

@Getter
public class DefectTypeExistsException extends RuntimeException{
    public DefectTypeExistsException(String message) {
        super(message);
    }

    public DefectTypeExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
