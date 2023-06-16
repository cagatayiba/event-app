package com.event.userservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApplicationUserRequest {
    @NotBlank(message = "Username cannot be null!!")
    private String username;
    @NotBlank(message = "Name cannot be null!!")
    private String password;
    @NotBlank(message = "Password cannot be null!!")
    @Email(message = "Email Format Is Not Valid")
    private String email;
}
