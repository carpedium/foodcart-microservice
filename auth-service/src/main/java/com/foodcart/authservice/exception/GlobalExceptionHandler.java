package com.foodcart.authservice.exception;

import com.foodcart.authservice.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorDto> handleUserExists(UserAlreadyExistsException ex) {
		return new ResponseEntity<>(new ErrorDto("USER_ALREADY_EXISTS", ex.getMessage()), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorDto> handleInvalidCreds(InvalidCredentialsException ex) {
		return new ResponseEntity<>(new ErrorDto("INVALID_CREDENTIALS", ex.getMessage()), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UnauthenticatedException.class)
	public ResponseEntity<ErrorDto> handleUnauthenticated(UnauthenticatedException ex) {
		return new ResponseEntity<>(new ErrorDto("UNAUTHORIZED", ex.getMessage()), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDto> handleAccessDenied(AccessDeniedException ex) {
		return new ResponseEntity<>(new ErrorDto("ACCESS_DENIED", ex.getMessage()), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorDto> handleRuntime(RuntimeException ex) {
		return new ResponseEntity<>(new ErrorDto("INTERNAL_ERROR", "Something went wrong"),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UserCreationException.class)
	public ResponseEntity<ErrorDto> handleUserCreationException(UserCreationException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorDto("USER_CREATION_ERROR", "Something went wrong"));
	}

}