package com.event.userservice.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProfileResponse {
    private String firstName;
    private String lastName;
    private String imgUrl;
    private String bioInformation;
}
