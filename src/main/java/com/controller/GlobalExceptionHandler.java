package com.controller;

import com.dto.CustomErrorResponse;
import com.exception.NoAvailableCopiesException;
import com.exception.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleRecordNotFound(RecordNotFoundException ex){
        var error = new CustomErrorResponse(ex.getMessage(), null);
        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidMethodArgument(MethodArgumentNotValidException ex){
        var error = new CustomErrorResponse("Bad request", null);
        return  ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(NoAvailableCopiesException.class)
    public ResponseEntity<CustomErrorResponse> handleNoAvailableCopies(NoAvailableCopiesException ex){
        var error = new CustomErrorResponse(ex.getMessage(), null);
        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

}
