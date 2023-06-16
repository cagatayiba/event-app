package com.event.userservice.dto;


import com.event.userservice.model.ApplicationUsers;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationUserRestrictedResponse {
    private String username;
    private String imageUrl;

    public static ApplicationUserRestrictedResponse fromApplicationUser(ApplicationUsers user) {
        return ApplicationUserRestrictedResponse.builder()
                .username(user.getUsername())
                .imageUrl("/api/v1/users/profile-img/" + user.getUsername())
                .build();
    }
}
