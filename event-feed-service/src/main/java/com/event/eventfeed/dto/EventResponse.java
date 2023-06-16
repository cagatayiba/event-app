package com.event.eventfeed.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class EventResponse {
    private String id;
    private String name;
    private String locationX;
    private String locationY;
    private String description;
    private LocalDateTime startDate;
    private String registerDueDate;
    private Integer userLimit;
    private String organizatorUsername;
}
