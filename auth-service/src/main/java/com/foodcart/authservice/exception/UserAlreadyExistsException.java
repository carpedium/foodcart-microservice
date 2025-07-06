package com.foodcart.authservice.exception;

public class UserAlreadyExistsException extends RuntimeException {
   
	public UserAlreadyExistsException() {
        super("Username or email already registered");
    }
    
	public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
