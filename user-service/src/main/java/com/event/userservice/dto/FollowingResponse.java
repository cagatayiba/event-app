package com.event.userservice.dto;


import com.event.userservice.model.ApplicationUsers;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FollowingResponse {
    private List<ApplicationUserRestrictedResponse> followingUsers;

    public static FollowingResponse fromApplicationUser(ApplicationUsers applicationUser){
        var followingList = applicationUser
                .getFollowing()
                .stream()
                .map(ApplicationUserRestrictedResponse::fromApplicationUser)
                .collect(Collectors.toList());
        return FollowingResponse.builder()
                .followingUsers(followingList)
                .build();
    }

}
