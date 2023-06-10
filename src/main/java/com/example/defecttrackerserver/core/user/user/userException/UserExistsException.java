package com.example.defecttrackerserver.core.user.user.userException;

import lombok.Getter;

@Getter
public class UserExistsException extends RuntimeException{
    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
