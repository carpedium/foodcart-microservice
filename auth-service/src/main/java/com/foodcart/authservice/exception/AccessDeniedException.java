package com.foodcart.authservice.exception;

public class AccessDeniedException extends RuntimeException {

	public AccessDeniedException(String msg) {
        super(msg);
    }
    
    public AccessDeniedException() {
        super("You do not have permission");
    }
}