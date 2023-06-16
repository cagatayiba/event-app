package com.event.eventfeed.api;


import com.event.eventfeed.dto.EventsInfoRestricted;
import com.event.eventfeed.dto.RemoveEventFromFeedRequest;
import com.event.eventfeed.dto.SimpleIsSuccessResponse;
import com.event.eventfeed.exception.UserNotFoundException;
import com.event.eventfeed.service.EventFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event-feed")
@RequiredArgsConstructor
public class EventFeedController {

    private final EventFeedService service;
    @GetMapping("/{username}")
    public ResponseEntity<EventsInfoRestricted> getUserFeed(@PathVariable String username){
        return ResponseEntity.ok().body(service.getFeed(username));
    }

    @GetMapping("/test/{username}")
    public ResponseEntity<List<String>> getUserFeedString(@PathVariable String username){
        return ResponseEntity.ok().body(service.getFeeds(username));
    }

    @PostMapping("/add-user/{username}")
    public void addUser(@PathVariable String username){
        service.addUser(username);
    }

    @PutMapping("/remove-event-from-feed")
    public SimpleIsSuccessResponse removeEventFromFeed(@RequestBody RemoveEventFromFeedRequest removeEventFromFeedRequest) throws UserNotFoundException {
        return SimpleIsSuccessResponse.builder()
                .isSuccess(service.removeEventFromFeed(removeEventFromFeedRequest))
                .build();

    }

}
