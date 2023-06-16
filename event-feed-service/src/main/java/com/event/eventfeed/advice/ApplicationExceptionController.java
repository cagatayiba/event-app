package com.event.eventfeed.advice;


import com.event.eventfeed.dto.GenericExceptionResponse;
import com.event.eventfeed.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionController {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GenericExceptionResponse> handleUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new GenericExceptionResponse(ex.getMessage()));

    }
}
