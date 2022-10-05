package com.druiz.fullstack.back.config.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<CustomError> responseUnprocessable (UnprocessableException exception){
        CustomError error = new CustomError(422,exception.getMessage(),new Date());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    public ResponseEntity<CustomError> responseNotFound (NotFoundException exception){
        CustomError error = new CustomError(404,exception.getMessage(),new Date());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
