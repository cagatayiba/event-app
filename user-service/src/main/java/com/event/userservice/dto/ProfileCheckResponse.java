package com.event.userservice.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProfileCheckResponse {
    private boolean isProfileExists;
}
