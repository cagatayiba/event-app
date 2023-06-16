package com.event.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    @NotBlank(message = "Username cannot be null or empty.")
    private String username;
    @NotBlank(message = "Password cannot be null or empty.")
    private String password;
}
