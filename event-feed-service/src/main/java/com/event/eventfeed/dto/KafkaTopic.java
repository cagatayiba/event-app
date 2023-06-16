package com.event.eventfeed.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class KafkaTopic {
	//created event topic
	private String id;
	private String organizatorUsername;

	//follow user topic
	private String follower;
	private String followee;
}
