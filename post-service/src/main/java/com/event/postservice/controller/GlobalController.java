package com.event.postservice.controller;


import com.event.postservice.exception.BussinessException;
import com.event.postservice.exception.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalController extends ResponseEntityExceptionHandler {
	@ExceptionHandler(BussinessException.class)
	public ResponseEntity<ErrorModel> customHandleBusinessException(BussinessException ex, WebRequest request) {
		ErrorModel error = ErrorModel.builder()
				.timestamp(ZonedDateTime.now())
				.status(ex.getErrorCode().getHttpCode())
				.error(ex.getErrorCode().toString())
				.message(ex.getMessage())
				.build();
		return new ResponseEntity<>(error, HttpStatus.resolve(error.getStatus()));
	}
}
