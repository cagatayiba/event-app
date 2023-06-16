package com.event.eventfeed.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ApplicationUserRestrictedResponse {
    private String username;
    private String imageUrl;

}
