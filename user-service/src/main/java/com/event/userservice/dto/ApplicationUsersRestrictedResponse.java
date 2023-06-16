package com.event.userservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUsersRestrictedResponse {
    private List<ApplicationUserRestrictedResponse> usersInfo;
}
