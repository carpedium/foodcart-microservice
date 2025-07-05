package com.foodcart.authservice.exception;

public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException() {
        super("Authorization header missing or invalid");
    }
}