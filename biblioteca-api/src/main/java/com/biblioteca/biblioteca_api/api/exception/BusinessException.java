package com.biblioteca.biblioteca_api.api.exception;

public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
}
