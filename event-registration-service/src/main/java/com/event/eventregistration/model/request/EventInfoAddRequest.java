package com.event.eventregistration.model.request;

import com.event.eventregistration.entity.Registration;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString
public class EventInfoAddRequest {
	private String id;
	private String organizatorUsername;
	private Integer userLimit;
	private Set<Registration> users = new HashSet<>();
}
