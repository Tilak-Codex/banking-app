package com.banfico.banking.exception;

public class CustomerNotFound extends RuntimeException {
    public CustomerNotFound(String message) {
        super(message);
    }    
    
}
