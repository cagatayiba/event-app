package com.event.userservice.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DefaultResponse {
    private boolean isSuccess;
}
