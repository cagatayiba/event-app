package com.event.userservice.dto;


import com.event.userservice.model.ApplicationUsers;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowerResponse {
    private List<ApplicationUserRestrictedResponse> followers;

    public static FollowerResponse fromApplicationUser(ApplicationUsers applicationUser){
        var followerList = applicationUser
                .getFollowers()
                .stream()
                .map(ApplicationUserRestrictedResponse::fromApplicationUser)
                .collect(Collectors.toList());
        return FollowerResponse.builder()
                .followers(followerList)
                .build();
    }
}
