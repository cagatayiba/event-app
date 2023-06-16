package com.event.eventmanagementservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
	@Column(length = 36, nullable = false, updatable = false)
	@Setter(AccessLevel.NONE)
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "location_x")
	private String locationX;

	@Column(name = "location_y")
	private String locationY;

	//FIXME LOB yap
	@Column(name = "description")
	private String description;

	@Column(name = "start_date")
	private LocalDateTime startDate;

	@Column(name = "register_due_date")
	private LocalDateTime registerDueDate;

	@Column(name = "user_limit")
	private Integer userLimit;

	@Column(name = "organizator_username")
	private String organizatorUsername;

	@Column(name = "image_path")
	private String imagePath;
}
