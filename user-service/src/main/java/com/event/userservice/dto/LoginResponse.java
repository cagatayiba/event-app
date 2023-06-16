package com.event.userservice.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {
    private boolean isSuccess;
    private boolean isProfileInfoFilled;
}
