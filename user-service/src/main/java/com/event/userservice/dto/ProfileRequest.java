package com.event.userservice.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProfileRequest {
    @NotBlank(message = "firstname is required")
    private String firstName;
    @NotBlank(message = "lastname is required")
    private String lastName;
    private String bioInformation;
}
