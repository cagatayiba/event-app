package com.event.eventfeed.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("feeds")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feed {
    @Id
    private String id;
    private String username;
    private List<String> feedEvents;
}

