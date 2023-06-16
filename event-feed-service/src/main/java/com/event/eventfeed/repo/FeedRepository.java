package com.event.eventfeed.repo;

import com.event.eventfeed.model.Feed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FeedRepository extends MongoRepository<Feed, String> {
    Optional<Feed> findByUsername(String username);

}
