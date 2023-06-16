package com.event.eventregistration.model.response;


import com.event.eventregistration.entity.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredEventsWithStatusResponse {
    private List<RegistrationInfoWithStatus> eventsWithRegistrationResponses;
}
