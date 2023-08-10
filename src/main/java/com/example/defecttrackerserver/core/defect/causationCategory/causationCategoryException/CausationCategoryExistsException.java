package com.example.defecttrackerserver.core.defect.causationCategory.causationCategoryException;

import lombok.Getter;

@Getter
public class CausationCategoryExistsException extends RuntimeException{
    public CausationCategoryExistsException(String message) {
        super(message);
    }

    public CausationCategoryExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
