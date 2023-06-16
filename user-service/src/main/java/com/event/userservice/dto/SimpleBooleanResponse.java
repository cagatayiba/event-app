package com.event.userservice.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class SimpleBooleanResponse {
    private Boolean isConditionTrue;
}
