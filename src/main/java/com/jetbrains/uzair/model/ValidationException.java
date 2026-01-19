package com.jetbrains.uzair.model;

public class ValidationException extends RuntimeException {
    public ValidationException(String message){
        super(message);
    }
}