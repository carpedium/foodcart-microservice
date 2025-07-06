package com.foodcart.authservice.exception;

public class InvalidCredentialsException extends RuntimeException {
    
    
    public InvalidCredentialsException(String msg) {
        super(msg);
    }
    
	public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}