package com.event.eventfeed.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetEventsRequest {
	private List<String> idList;
}
