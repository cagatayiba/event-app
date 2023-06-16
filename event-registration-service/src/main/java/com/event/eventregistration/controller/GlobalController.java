package com.event.eventregistration.controller;

import com.event.eventregistration.exception.BusinessException;
import com.event.eventregistration.exception.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalController extends ResponseEntityExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorModel> customHandleBusinessException(BusinessException ex, WebRequest request) {
		ErrorModel error = ErrorModel.builder()
				.timestamp(ZonedDateTime.now())
				.status(ex.getErrorCode().getHttpCode())
				.error(ex.getErrorCode().toString())
				.message(ex.getMessage())
				.build();
		return new ResponseEntity<>(error, HttpStatus.resolve(error.getStatus()));
	}
}
