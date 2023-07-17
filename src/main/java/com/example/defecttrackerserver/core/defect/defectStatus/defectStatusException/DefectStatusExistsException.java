package com.example.defecttrackerserver.core.defect.defectStatus.defectStatusException;

import lombok.Getter;

@Getter
public class DefectStatusExistsException extends RuntimeException{
    public DefectStatusExistsException(String message) {
        super(message);
    }

    public DefectStatusExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
