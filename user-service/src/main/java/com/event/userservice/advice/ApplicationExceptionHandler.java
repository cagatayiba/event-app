package com.event.userservice.advice;

import com.event.userservice.dto.GenericBadRequestResponse;
import com.event.userservice.exceptions.GenericBadRequestException;
import com.event.userservice.exceptions.ProfileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(GenericBadRequestException.class)
    public ResponseEntity<GenericBadRequestResponse> handleGenericBadRequestException(GenericBadRequestException ex){
        GenericBadRequestResponse resBody = GenericBadRequestResponse.builder()
                .isSuccess(false)
                .failReason(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(resBody);
    }
    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<GenericBadRequestResponse> handleProfileNotFoundException(ProfileNotFoundException ex){
        GenericBadRequestResponse resBody = GenericBadRequestResponse.builder()
                .isSuccess(false)
                .failReason(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(resBody);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<GenericBadRequestResponse> handleFileSizeException(MaxUploadSizeExceededException ex){
        GenericBadRequestResponse resBody = GenericBadRequestResponse.builder()
                .isSuccess(false)
                .failReason(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(resBody);
    }
}
