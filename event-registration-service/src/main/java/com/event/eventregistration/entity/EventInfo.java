package com.event.eventregistration.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Document("event_infos")
@Builder
public class EventInfo {
	private String id;

	@Field("organizator_username")
	private String organizatorUsername;

	@Field("user_limit")
	private Integer userLimit;

	@Field("users")
	private Set<Registration> users;

	public Optional<Registration> findUser(String username){
		for (Registration registration: users){
			if (registration.getUsername().equals(username)){
				return Optional.of(registration);
			}
		}
		return Optional.empty();
	}
}
