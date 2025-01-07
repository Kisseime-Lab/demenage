package com.startup.demenage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalCatcher {
    
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleException(Exception e, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
