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
public class EventResponseRestricted {
    private String name;
    private LocalDateTime startDate;
    private String uuid;
    private String imageUrl;


}
