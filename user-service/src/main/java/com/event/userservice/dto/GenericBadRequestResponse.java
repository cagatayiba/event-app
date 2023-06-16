package com.event.userservice.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GenericBadRequestResponse {
    private boolean isSuccess;
    private String failReason;
}
