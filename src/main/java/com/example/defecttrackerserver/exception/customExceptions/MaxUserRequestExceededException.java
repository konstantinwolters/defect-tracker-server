package com.example.defecttrackerserver.exception.customExceptions;

public class MaxUserRequestExceededException extends RuntimeException{
    public MaxUserRequestExceededException(String message){
        super(message);
    }
}
