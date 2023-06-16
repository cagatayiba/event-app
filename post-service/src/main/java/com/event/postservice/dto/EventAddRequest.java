package com.event.postservice.dto;

import com.event.postservice.model.Post;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class EventAddRequest {
	private String id;
	private List<Post> posts = new ArrayList<>();
}
