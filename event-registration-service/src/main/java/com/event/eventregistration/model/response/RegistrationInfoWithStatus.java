package com.event.eventregistration.model.response;


import com.event.eventregistration.entity.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationInfoWithStatus {
    private EventResponseRestricted event;
    private RegistrationStatus registrationStatus;
}
