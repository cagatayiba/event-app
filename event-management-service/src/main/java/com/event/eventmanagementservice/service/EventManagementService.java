package com.event.eventmanagementservice.service;

import com.event.eventmanagementservice.entity.Event;
import com.event.eventmanagementservice.exception.BusinessException;
import com.event.eventmanagementservice.exception.ErrorCode;
import com.event.eventmanagementservice.model.response.*;
import com.event.eventmanagementservice.topic.CreatedEvents;
import com.event.eventmanagementservice.model.request.EventAddRequest;
import com.event.eventmanagementservice.model.request.GetEventsRequest;
import com.event.eventmanagementservice.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventManagementService {
	private final EventRepository eventRepository;
	private final KafkaTemplate<String, CreatedEvents> kafkaTemplate;
	@Value("${events.image.path}")
	private String EVENTS_IMAGE_PATH;


	public EventsInfoRestrictedWithDueInfo getEventsOfOrganizator(String organizatorUsername){
		LocalDateTime currentDate = LocalDateTime.now(ZoneId.of ( "Europe/Istanbul" ));
		var eventResponseList = eventRepository.findByOrganizatorUsername(organizatorUsername).stream()
				.map(event -> {
					boolean isClosed = currentDate.isAfter(event.getStartDate());
					return EventResponseRestrictedWithDueInfo.fromEvent(event, isClosed);
				}).toList();
		return EventsInfoRestrictedWithDueInfo.builder()
				.events(eventResponseList)
				.build();

	}

	public List<EventResponse> getEventsByIds(GetEventsRequest getEventsRequest) {
		return eventRepository.findAllByIds(getEventsRequest.getIdList()).stream().map(EventResponse::fromEntity).toList();
	}

	public EventResponse getEvent(String id){
		Event election = eventRepository.findById(id)
				.orElseThrow(() -> new BusinessException("Event not found.", ErrorCode.resource_missing));
		return EventResponse.fromEntity(election);
	}
	public EventResponse addEvent(EventAddRequest eventAddRequest){
		Event event = fromRequest(new Event(), eventAddRequest);
		eventRepository.save(event);
		CreatedEvents createdEvents = new CreatedEvents(event.getId(), event.getOrganizatorUsername(), event.getUserLimit());
		kafkaTemplate.send("createdEvents", createdEvents);
		return EventResponse.fromEntity(event);
	}

	public EventResponse deleteEvent(String id) {
		Event event = eventRepository.findById(id)
				.orElseThrow(() -> new BusinessException("Event not found.", ErrorCode.resource_missing));
		eventRepository.delete(event);
		return EventResponse.fromEntity(event);
	}

	private Event fromRequest(Event event, EventAddRequest eventAddRequest){
		event.setName(eventAddRequest.getName());
		event.setLocationX(eventAddRequest.getLocationX());
		event.setLocationY(eventAddRequest.getLocationY());
		event.setDescription(eventAddRequest.getDescription());
		event.setStartDate(eventAddRequest.getStartDate());
		event.setRegisterDueDate(eventAddRequest.getRegisterDueDate());
		event.setUserLimit(eventAddRequest.getUserLimit());
		event.setOrganizatorUsername(eventAddRequest.getOrganizatorUsername());
		event.setImagePath(null);

		return event;
	}

    public Page<EventResponse> getCurrentEventsBySearchCriteria(String searchKey, Pageable pageable) {
		return eventRepository.findCurrentEventsBySearchKey(searchKey, LocalDateTime.now(ZoneId.of ( "Europe/Istanbul" )), pageable)
				.map(EventResponse::fromEntity);
    }

	public EventsInfoRestricted getCurrentEventsInformation(GetEventsRequest getEventsRequest) {
		var eventResponseList = eventRepository.findByIdInAndStartDateAfter(getEventsRequest.getIdList(), LocalDateTime.now(ZoneId.of ( "Europe/Istanbul" )))
				.stream()
				.map(EventResponseRestricted::fromEvent)
				.toList();
		return EventsInfoRestricted.builder()
				.events(eventResponseList)
				.build();
	}

	public byte[] getEventImage(String eventUUID) throws IOException {
		var eventOptional = eventRepository.findById(eventUUID);
		var event =  eventOptional.orElseThrow(() -> new BusinessException("Event not found.", ErrorCode.resource_missing));
		String filePath = event.getImagePath();
		//FIXME null file
		byte[] imageInByte = Files.readAllBytes(new File(filePath).toPath());
		return imageInByte;
	}

	@Transactional
	public void addEventImage(MultipartFile img, String eventUUID) throws IOException {
		String absoluteImgPath = EVENTS_IMAGE_PATH + "/" + eventUUID;
		img.transferTo(new File(absoluteImgPath));
		var event = eventRepository.findById(eventUUID)
				.orElseThrow(() -> new BusinessException("Event not found.", ErrorCode.resource_missing));
		event.setImagePath(absoluteImgPath);
		eventRepository.save(event);
	}

	public EventsInfoRestricted getClosedEventsInformation(GetEventsRequest eventsRequest) {
		List<Event> closedEvents = eventRepository
				.findByIdInAndStartDateBefore(
						eventsRequest.getIdList(),
						LocalDateTime.now(ZoneId.of ( "Europe/Istanbul" ))
				);
		var eventResponses = closedEvents.stream()
				.map(EventResponseRestricted::fromEvent).toList();

		return EventsInfoRestricted.builder()
				.events(eventResponses)
				.build();

	}


	public EventsInfoRestricted getRandomEvents(){
		var events = eventRepository
				.findByStartDateAfterOrderByStartDateAsc(LocalDateTime.now(ZoneId.of ( "Europe/Istanbul" )));
		if(events.size() > 10){
			events = events.subList(0,10);
		}

		return EventsInfoRestricted.builder()
				.events(events.stream().map(EventResponseRestricted::fromEvent).toList())
				.build();


	}
}
