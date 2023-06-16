package com.event.postservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document
@Builder
@Getter
@Setter
public class Post {
	@Field
	private String username;
	@Field
	private String body;
}
