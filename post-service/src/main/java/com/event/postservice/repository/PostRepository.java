package com.event.postservice.repository;

import com.event.postservice.model.EventPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends MongoRepository<EventPost, String> {
}
