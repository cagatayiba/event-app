package com.event.eventregistration.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Builder
@Getter
@Setter
public class Registration {

	@Field
	private String username;
	@Field
	private RegistrationStatus status;
}
