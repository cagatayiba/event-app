package com.event.eventfeed.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericExceptionResponse {
    private boolean isSuccess;
    private String failReason;

    public GenericExceptionResponse(String failReason){
        this.failReason = failReason;
        this.isSuccess = false;
    }
}
