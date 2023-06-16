package com.event.postservice.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AddPostRequest {
	private String eventId;
	private String username;
	private String body;
}
