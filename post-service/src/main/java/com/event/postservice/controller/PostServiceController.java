package com.event.postservice.controller;

import com.event.postservice.dto.AddPostRequest;
import com.event.postservice.model.Post;
import com.event.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostServiceController {
	private final PostService service;

	@GetMapping("/{eventId}")
	public ResponseEntity<List<Post>> getEventPost(@PathVariable String eventId){
		return ResponseEntity.ok().body(service.getEventPosts(eventId));
	}

	@PostMapping("/add-post")
	public void addPost(@RequestBody AddPostRequest addPostRequest){
		service.addPost(addPostRequest);
	}

}
