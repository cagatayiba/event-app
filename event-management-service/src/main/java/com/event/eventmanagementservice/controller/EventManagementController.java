package com.event.eventmanagementservice.controller;

import com.event.eventmanagementservice.model.request.EventAddRequest;
import com.event.eventmanagementservice.model.request.GetEventsRequest;
import com.event.eventmanagementservice.model.response.EventResponse;
import com.event.eventmanagementservice.model.response.EventsInfoRestricted;
import com.event.eventmanagementservice.model.response.EventsInfoRestrictedWithDueInfo;
import com.event.eventmanagementservice.service.EventManagementService;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/event-management")
public class EventManagementController {
	private final EventManagementService eventManagementService;


	// getting all events of an organizator with due date info
	@GetMapping("/get-by-organizator/{organizatorUsername}")
	public EventsInfoRestrictedWithDueInfo getEventsOfOrganizator(@PathVariable String organizatorUsername) {
		return eventManagementService.getEventsOfOrganizator(organizatorUsername);
	}


	// searching current events by a keyword
	@GetMapping("/get-current-events/by-name/{searchKey}")
	public Page<EventResponse> getEventsBySearchCriteria(Pageable pageable, @PathVariable String searchKey){
		return eventManagementService.getCurrentEventsBySearchCriteria(searchKey, pageable);
	}

	// getting current event information (not closed) from a list of uuids
	@PostMapping("/get-current-events/from-uuid-list")
	public EventsInfoRestricted getCurrentEventsInformation(@RequestBody GetEventsRequest getEventsRequest){
		return eventManagementService.getCurrentEventsInformation(getEventsRequest);
	}

	// getting the image of the event by its uuid
	@GetMapping("/get-event-image/{eventUUID}")
	public ResponseEntity<?> getEventImage(@PathVariable String eventUUID) throws IOException {
		byte[] imgData = eventManagementService.getEventImage(eventUUID);
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.valueOf("image/png"))
				.body(imgData);
	}

	// getting the event information (closed) from list of uuids
	@PostMapping("/get-closed-events/from-uuid-list")
	public EventsInfoRestricted getClosedEventsInformation(@RequestBody GetEventsRequest eventsRequest){
		return eventManagementService.getClosedEventsInformation(eventsRequest);
	}

	@GetMapping("/get-random-events")
	public EventsInfoRestricted getRandomEvents(){
		return eventManagementService.getRandomEvents();
	}


	@PostMapping("/id")
	public List<EventResponse> getEventsByIds(@RequestBody GetEventsRequest getEventsRequest){
		return eventManagementService.getEventsByIds(getEventsRequest);
	}

	@GetMapping("/getById/{id}")
	public EventResponse getEvent(@PathVariable String id) {
		return eventManagementService.getEvent(id);
	}


	@PostMapping
	public EventResponse addEvent(@RequestBody EventAddRequest eventAddRequest){
		return eventManagementService.addEvent(eventAddRequest);
	}

	@PostMapping("/event-img/{eventUUID}")
	public void addEventImage(@PathVariable String eventUUID, @RequestParam("image") MultipartFile file) throws IOException {
		eventManagementService.addEventImage(file, eventUUID);
	}

	@DeleteMapping("/{id}")
	public EventResponse deleteEvent(@PathVariable String id) {
		return eventManagementService.deleteEvent(id);
	}
}
