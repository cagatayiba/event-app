package com.event.eventmanagementservice.advice;

import com.event.eventmanagementservice.model.response.GenericBadRequestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class ApplicationExceptionHandler {
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
