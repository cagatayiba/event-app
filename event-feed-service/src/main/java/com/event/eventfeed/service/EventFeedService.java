package com.event.eventfeed.service;


import com.event.eventfeed.dto.*;
import com.event.eventfeed.exception.UserNotFoundException;
import com.event.eventfeed.model.Feed;
import com.event.eventfeed.repo.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventFeedService {

    private final FeedRepository feedRepository;
    private final MongoTemplate mongoTemplate;
    private final WebClient.Builder webClientBuilder;

    public EventsInfoRestricted getFeed(String username) {
        List<String> uuids = new ArrayList<>();
        feedRepository.findByUsername(username).ifPresent(feed ->{
             uuids.addAll(feed.getFeedEvents());
        });
        EventsInfoRestricted eventResponses = webClientBuilder.build().post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("event-management-service")
                        .path("/api/v1/event-management/get-current-events/from-uuid-list")
                        .build())
                .body(BodyInserters.fromValue(GetEventsRequest.builder()
                                                .idList(uuids)
                                                .build()))
                .retrieve()
                .bodyToMono(EventsInfoRestricted.class)
                .block();

        return eventResponses;
    }

    public List<String> getFeeds(String username){
        var feedOptional  = feedRepository.findByUsername(username);
        if(feedOptional.isPresent()){
            return feedOptional.get().getFeedEvents();
        }
        return new ArrayList<>();
    }

    @KafkaListener(topics = "createdEvents")
    public void addFeed(KafkaTopic kafkaEventRequest){
        FollowerResponse followerResponse = webClientBuilder.build().get()
                .uri(uriBuilder -> uriBuilder
                    .scheme("http")
                    .host("user-service")
                    .path("/api/v1/users/follower-list/{username}")
                    .build(kafkaEventRequest.getOrganizatorUsername()))
                .retrieve()
                .bodyToMono(FollowerResponse.class)
                .block();
        List<String> usernames = followerResponse.getFollowers().stream().map(ApplicationUserRestrictedResponse::getUsername).collect(Collectors.toList());
        Query query = Query.query(Criteria.where("username").in(usernames));
        Update update = new Update().addToSet("feedEvents", kafkaEventRequest.getId());
        mongoTemplate.updateMulti(query, update, Feed.class);

    }

    public void addUser(String username) {
        feedRepository.save(Feed.builder()
                    .username(username)
                    .feedEvents(new ArrayList<>())
                    .build());
    }


    @Transactional
    public boolean removeEventFromFeed(RemoveEventFromFeedRequest removeEventFromFeedRequest) throws UserNotFoundException {
        String eventUUID = removeEventFromFeedRequest.getUuid();
        String username = removeEventFromFeedRequest.getUsername();
        var feed = feedRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with this username: " + username));
        List<String> feedEventsUpdated = new ArrayList<>();
        List<String> feedEventsOld = feed.getFeedEvents();
        for (String eventUUIDOld : feedEventsOld) {
            if (!eventUUIDOld.equals(eventUUID)) {
                feedEventsUpdated.add(eventUUIDOld);
            }
        }
        feed.setFeedEvents(feedEventsUpdated);
        mongoTemplate.save(feed);
        return true;
    }

    @KafkaListener(topics = "followUser")
    public void kafkaFollowUser(KafkaTopic kafkaTopic) throws UserNotFoundException {
        String follower = kafkaTopic.getFollower();
        String followee = kafkaTopic.getFollowee();
        var response = webClientBuilder.build().get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("event-management-service")
                        .path("/api/v1/event-management/get-by-organizator/"+followee)
                        .build())
                .retrieve()
                .bodyToMono(EventsInfoRestrictedWithDueInfo.class)
                .block();

        List<String> followeeEvents = response.getEvents().stream().filter((event) -> !event.isClosed()).
                map(EventResponseRestrictedWithDueInfo::getEventUUID).toList();
        var followerFeed = feedRepository.findByUsername(follower).orElseThrow(() ->new UserNotFoundException("User not found with this username"+ follower));

        List<String> updatedEvents = followerFeed.getFeedEvents();
        for (String newEvent: followeeEvents){
            updatedEvents.add(newEvent);
        }
        followerFeed.setFeedEvents(updatedEvents);
        feedRepository.save(followerFeed);
    }
}
