package com.event.eventfeed.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class FeedRequest {
    private String username;
    private List<String> feedEvents;
}
