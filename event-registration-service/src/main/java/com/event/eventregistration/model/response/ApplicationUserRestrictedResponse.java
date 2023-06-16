package com.event.eventregistration.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUserRestrictedResponse {
    private String username;
    private String imageUrl;
}
