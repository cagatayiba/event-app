package com.event.eventfeed.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@Getter
@Setter
@Builder
public class FeedResponse {
    private List<EventResponse> events;
}
