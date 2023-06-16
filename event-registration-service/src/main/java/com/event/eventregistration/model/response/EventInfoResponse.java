package com.event.eventregistration.model.response;

import com.event.eventregistration.entity.EventInfo;
import com.event.eventregistration.entity.Registration;
import lombok.*;

import java.util.Set;
import java.util.Map;
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class EventInfoResponse {
	private String id;
	private String organizatorUsername;
	private Integer userLimit;
	private Set<Registration> users;

	public static EventInfoResponse fromEntity(EventInfo eventInfo){
		return EventInfoResponse.builder()
				.id(eventInfo.getId())
				.organizatorUsername(eventInfo.getOrganizatorUsername())
				.userLimit(eventInfo.getUserLimit())
				.users(eventInfo.getUsers())
				.build();
	}
}
