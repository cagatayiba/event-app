package com.event.postservice.service;

import com.event.postservice.dto.AddPostRequest;
import com.event.postservice.dto.EventAddRequest;
import com.event.postservice.exception.BussinessException;
import com.event.postservice.exception.ErrorCode;
import com.event.postservice.model.EventPost;
import com.event.postservice.repository.PostRepository;
import com.event.postservice.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;

	public List<Post> getEventPosts(String eventId) {
		EventPost eventPost = postRepository.findById(eventId)
				.orElseThrow(() -> new BussinessException("Event not found.", ErrorCode.resource_missing));
		return eventPost.getPosts();
	}

	@Transactional
	public void addPost(AddPostRequest addPostRequest) {
		EventPost eventPost = postRepository.findById(addPostRequest.getEventId())
				.orElseThrow(() -> new BussinessException("Event not found.", ErrorCode.resource_missing));
		for (Post post: eventPost.getPosts()){
			if(post.getUsername().equals(addPostRequest.getUsername())){
				throw new BussinessException("User has already posted for this event.", ErrorCode.not_allowed);
			}
		}
		Post post = Post.builder()
				.username(addPostRequest.getUsername())
				.body(addPostRequest.getBody())
				.build();
		eventPost.getPosts().add(post);
		postRepository.save(eventPost);
	}

	@KafkaListener(topics = "createdEvents")
	public void addEvent(EventAddRequest eventAddRequest){
		EventPost eventPost = EventPost.builder()
				.id(eventAddRequest.getId())
				.posts(eventAddRequest.getPosts())
				.build();
		postRepository.save(eventPost);
	}
}
