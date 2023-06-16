package com.event.userservice.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddProfileInfoResponse {
    private boolean isSuccess;
    private String addImgUrl;
}
