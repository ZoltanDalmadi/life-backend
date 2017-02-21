package com.github.life.controller;

import com.github.life.errors.InvalidLifFileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

	@ExceptionHandler
	public ResponseEntity<String> handleInvalidLifFile(InvalidLifFileException e) {
		return ResponseEntity.unprocessableEntity().body(e.getMessage());
	}

	@ExceptionHandler
	public ResponseEntity<String> handleIOException(IOException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

}
