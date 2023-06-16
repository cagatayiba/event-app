package com.event.eventmanagementservice.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class EventAddRequest {
	private String name;
	private String locationX;
	private String locationY;
	private String description;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startDate;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime registerDueDate;
	private Integer userLimit;
	private String organizatorUsername;
}
