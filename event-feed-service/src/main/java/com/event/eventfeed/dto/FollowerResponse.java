package com.event.eventfeed.dto;



import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FollowerResponse {
    private List<ApplicationUserRestrictedResponse> followers;

}
