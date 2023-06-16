package com.event.postservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Getter
@Setter
@Document("event_posts")
@Builder
public class EventPost {
	private String id;
	@Field("posts")
	private List<Post> posts;
}
