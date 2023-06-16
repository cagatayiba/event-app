package com.event.eventregistration.model.response;


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
