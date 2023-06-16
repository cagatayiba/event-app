package com.event.eventregistration.controller;

import com.event.eventregistration.entity.RegistrationStatus;
import com.event.eventregistration.model.request.AnswerRegistrationRequest;
import com.event.eventregistration.model.request.RegistrationAddRequest;
import com.event.eventregistration.model.response.*;
import com.event.eventregistration.service.EventRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/event-registration")
public class EventRegistrationController {
	private final EventRegistrationService eventRegistrationService;

	@GetMapping("/{id}")
	public EventInfoResponse getEventRegistration(@PathVariable String id) {
		return eventRegistrationService.getEventRegistration(id);
	}


	// getting the waiting registration number for an event
	@GetMapping("/registered-users-count/waiting/{eventUUID}")
	public SimpleCountResponse getRegistrationCountOfEvent(@PathVariable String eventUUID) {
		return SimpleCountResponse.builder()
				.count(eventRegistrationService.getRegistrationCountOfEvent(eventUUID))
				.build();
	}

	// getting registered users for an event (only accepted)
	@GetMapping("/registered-users/{eventUUID}")
	public ApplicationUsersRestrictedResponse getRegisteredUsersForAnEvent(@PathVariable String eventUUID){
		return eventRegistrationService.getRegisteredUsersForAnEvent(eventUUID, RegistrationStatus.ACCEPTED);
	}

	// getting the users that made a registration (status = WAITING) request to an event
	@GetMapping("/waiting-registrations/{eventUUID}")
	public ApplicationUsersRestrictedResponse getWaitingRegisteredUsersForAnEvent(@PathVariable String eventUUID){
		return eventRegistrationService.getRegisteredUsersForAnEvent(eventUUID, RegistrationStatus.WAITING);
	}

	/*
		Getting the registered (registrationStatus == ACCEPTED)events of a user.
		If isClosed sent as true only closed event will return. By closed events
		we mean the events that are already happened and the user is joined to. If isClosed
		flag sent as false the events that will happen future, the user registered and accepted to
		will return.
	*/
	@GetMapping("/registered-events/accepted/{username}/{isClosed}")
	public EventsInfoRestricted getEventsThatAUserRegistered(
			@PathVariable String username,
			@PathVariable("isClosed") boolean isClosed
	){
		return eventRegistrationService.getEventsThatAUserRegistered(username, isClosed);
	}


	/*
		All registered events and currently organized events (not happened yet)
		 will be returned from that api regardless of the status.
	*/
	@GetMapping("/registered-events/all-status/{username}")
	public RegisteredEventsWithStatusResponse getEventsThatAUserMadeRegistrationRequest(@PathVariable String username){
		return RegisteredEventsWithStatusResponse.builder()
				.eventsWithRegistrationResponses(eventRegistrationService.getEventsThatAUserMadeRegistrationRequest(username))
				.build();
	}

	@PostMapping("/add-registration")
	public EventInfoResponse addRegistration(@RequestBody RegistrationAddRequest registrationAddRequest){
		return eventRegistrationService.addRegistration(registrationAddRequest);
	}

	@PutMapping("/accept-request")
	public EventInfoResponse acceptRegistration(@RequestBody AnswerRegistrationRequest answerRegistrationRequest){
		return eventRegistrationService.acceptRegistration(answerRegistrationRequest);
	}

	@PutMapping("/reject-request")
	public EventInfoResponse rejectRegistration(@RequestBody AnswerRegistrationRequest answerRegistrationRequest){
		return eventRegistrationService.rejectRegistration(answerRegistrationRequest);
	}

}
