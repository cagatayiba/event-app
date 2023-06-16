package com.event.eventregistration.model.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AnswerRegistrationRequest {
	private String organizatorUsername;
	private String userUsername;
	private String eventId;
}
