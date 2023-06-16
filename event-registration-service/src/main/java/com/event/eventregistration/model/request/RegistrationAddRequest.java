package com.event.eventregistration.model.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegistrationAddRequest {
	private String eventId;
	private String username;

}
