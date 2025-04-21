package com.startup.demenage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.startup.demenage.model.ErrorApiModel;

@ControllerAdvice
public class GlobalCatcher {

    @ExceptionHandler({ ValidationInputException.class })
    public ResponseEntity<ErrorApiModel> handle_validation_input_exception(Exception e) {
        return new ResponseEntity<ErrorApiModel>(
                new ErrorApiModel().code(HttpStatus.UNPROCESSABLE_ENTITY.name()).message(e.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({ InvalidInputException.class, UsernameNotFoundException.class })
    public ResponseEntity<ErrorApiModel> handle_invalid_input_exception(Exception e) {
        return new ResponseEntity<ErrorApiModel>(
                new ErrorApiModel().code(HttpStatus.BAD_REQUEST.name()).message(e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorApiModel> handle_illegal_argument_exception(MethodArgumentNotValidException e) {
        return new ResponseEntity<ErrorApiModel>(
                new ErrorApiModel()
                        .code(HttpStatus.UNPROCESSABLE_ENTITY.name())
                        .message(e.getFieldError().getField() + " " + e.getFieldError().getDefaultMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorApiModel> handleException(Exception e) {
        ErrorApiModel errorDetails = new ErrorApiModel()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
