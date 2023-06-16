package com.event.eventfeed.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseRestrictedWithDueInfo {
    private String name;
    private LocalDateTime startDate;
    private boolean isClosed;
    private String imageUrl;

    private String eventUUID;

}
