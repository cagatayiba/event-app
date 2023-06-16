package com.event.eventmanagementservice.topic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedEvents {
	private String id;
	private String organizatorUsername;
	private Integer userLimit;
}
