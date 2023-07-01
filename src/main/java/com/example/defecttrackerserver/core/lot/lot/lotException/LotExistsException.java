package com.example.defecttrackerserver.core.lot.lot.lotException;

import lombok.Getter;

@Getter
public class LotExistsException extends RuntimeException{
    public LotExistsException(String message) {
        super(message);
    }

    public LotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
