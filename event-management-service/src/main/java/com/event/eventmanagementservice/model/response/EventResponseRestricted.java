package com.event.eventmanagementservice.model.response;


import com.event.eventmanagementservice.entity.Event;
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

    public static EventResponseRestricted fromEvent(Event event){
        return EventResponseRestricted.builder()
                .name(event.getName())
                .startDate(event.getStartDate())
                .uuid(event.getId())
                .imageUrl("/api/v1/event-management/get-event-image/" + event.getId())
                .build();
    }
}
