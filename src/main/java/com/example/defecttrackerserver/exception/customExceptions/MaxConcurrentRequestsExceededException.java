package com.example.defecttrackerserver.exception.customExceptions;

public class MaxConcurrentRequestsExceededException extends RuntimeException{
    public MaxConcurrentRequestsExceededException(String message){
        super(message);
    }
}
