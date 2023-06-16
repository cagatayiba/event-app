package com.event.eventregistration.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorModel {

    private ZonedDateTime timestamp;
    private int status;
    private String error;
    private String message;


    public ErrorModel() {

    }
}
