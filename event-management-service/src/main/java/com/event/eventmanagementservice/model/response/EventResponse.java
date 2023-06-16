package com.event.eventmanagementservice.model.response;

import com.event.eventmanagementservice.entity.Event;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class EventResponse {
	private String id;
	private String name;
	private String locationX;
	private String locationY;
	private String description;
	private LocalDateTime startDate;
	private LocalDateTime registerDueDate;
	private Integer userLimit;
	private String organizatorUsername;
	private String imageUrl;

	public static EventResponse fromEntity(Event event){
		return EventResponse.builder()
				.id(event.getId())
				.name(event.getName())
				.locationX(event.getLocationX())
				.locationY(event.getLocationY())
				.description(event.getDescription())
				.startDate(event.getStartDate())
				.registerDueDate(event.getRegisterDueDate())
				.userLimit(event.getUserLimit())
				.organizatorUsername(event.getOrganizatorUsername())
				.imageUrl("/api/v1/event-management/get-event-image/" + event.getId())
				.build();
	}
}
