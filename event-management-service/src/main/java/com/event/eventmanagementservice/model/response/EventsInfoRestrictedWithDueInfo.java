package com.event.eventmanagementservice.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventsInfoRestrictedWithDueInfo {
    List<EventResponseRestrictedWithDueInfo> events;
}