package com.event.eventregistration.service;

import com.event.eventregistration.entity.EventInfo;
import com.event.eventregistration.entity.Registration;
import com.event.eventregistration.entity.RegistrationStatus;
import com.event.eventregistration.exception.BusinessException;
import com.event.eventregistration.exception.ErrorCode;
import com.event.eventregistration.model.request.*;
import com.event.eventregistration.model.response.*;
import com.event.eventregistration.repository.RegistrationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class EventRegistrationService {
	private final RegistrationRepository registrationRepository;
	private final MongoTemplate mongoTemplate;
	private final WebClient.Builder webClientBuilder;

	public EventInfoResponse getEventRegistration(String id) {
		EventInfo eventInfo = registrationRepository.findById(id)
				.orElseThrow(() -> new BusinessException("Event not found.", ErrorCode.resource_missing));
		return EventInfoResponse.fromEntity(eventInfo);
	}
	@KafkaListener(topics = "createdEvents")
	public EventInfoResponse addEvent(EventInfoAddRequest eventInfoAddRequest){
		EventInfo eventInfo = fromRequest(eventInfoAddRequest);
		registrationRepository.save(eventInfo);
		return EventInfoResponse.fromEntity(eventInfo);
	}

	public ApplicationUsersRestrictedResponse getRegisteredUsersForAnEvent(String eventUUID, RegistrationStatus statusCriteria){
		EventInfo eventInfo = registrationRepository.findById(eventUUID).orElse(null);
		if (eventInfo == null){
			return ApplicationUsersRestrictedResponse.builder()
					.usersInfo(new ArrayList<>())
					.build();
		}

		var users =  eventInfo.getUsers().stream()
				.filter(registration -> registration.getStatus() == statusCriteria)
				.map(Registration::getUsername)
				.collect(Collectors.toList());

		// get user information as list
		var usersInfo = webClientBuilder.build().post()
				.uri(uriBuilder -> uriBuilder
						.scheme("http")
						.host("user-service")
						.path("/api/v1/users/users-info-restricted/all")
						.build())
				.body(BodyInserters.fromValue(ApplicationUsersRestrictedInfoRequest.builder()
						.usernames(users)
						.build()))
				.retrieve()
				.bodyToMono(ApplicationUsersRestrictedResponse.class)
				.block();

		return usersInfo;


	}

	@Transactional
	public EventInfoResponse addRegistration(RegistrationAddRequest registrationAddRequest) {
		EventInfo eventInfo = registrationRepository.findById(registrationAddRequest.getEventId())
				.orElseThrow(() -> new BusinessException("Event not found.", ErrorCode.resource_missing));

		Registration registration = fromRequest(registrationAddRequest);
		eventInfo.getUsers().add(registration);
		registrationRepository.save(eventInfo);
		// delete from the feed
		SimpleIsSuccessResponse response = webClientBuilder.build().put()
				.uri(uriBuilder -> uriBuilder
						.scheme("http")
						.host("event-feed-service")
						.path("/api/v1/event-feed/remove-event-from-feed")
						.build())
				.body(BodyInserters.fromValue(RemoveEventFromFeedRequest.builder()
						.uuid(eventInfo.getId())
						.username(registrationAddRequest.getUsername()).build()))
				.retrieve()
				.bodyToMono(SimpleIsSuccessResponse.class)
				.block();
		return EventInfoResponse.fromEntity(eventInfo);
	}

	public EventInfoResponse acceptRegistration(AnswerRegistrationRequest answerRegistrationRequest) {
		EventInfo eventInfo = registrationRepository.findById(answerRegistrationRequest.getEventId())
				.orElseThrow(() -> new BusinessException("Event not found.", ErrorCode.resource_missing));
		Registration registration = eventInfo.findUser(answerRegistrationRequest.getUserUsername())
				.orElseThrow(() -> new BusinessException("User not registered.", ErrorCode.resource_missing));
		if (eventInfo.getOrganizatorUsername().equals(answerRegistrationRequest.getOrganizatorUsername())){
			registration.setStatus(RegistrationStatus.ACCEPTED);
		}else {
			throw new BusinessException("Only organizator accept the registration request.", ErrorCode.unauthorized);
		}
		registrationRepository.save(eventInfo);
		return EventInfoResponse.fromEntity(eventInfo);
	}

	public EventInfoResponse rejectRegistration(AnswerRegistrationRequest answerRegistrationRequest) {
		EventInfo eventInfo = registrationRepository.findById(answerRegistrationRequest.getEventId())
				.orElseThrow(() -> new BusinessException("Event not found.", ErrorCode.resource_missing));
		Registration registration = eventInfo.findUser(answerRegistrationRequest.getUserUsername())
				.orElseThrow(() -> new BusinessException("User not registered.", ErrorCode.resource_missing));
		if (eventInfo.getOrganizatorUsername().equals(answerRegistrationRequest.getOrganizatorUsername())){
			registration.setStatus(RegistrationStatus.REJECTED);
		}else {
			throw new BusinessException("Only organizator reject the registration request.", ErrorCode.unauthorized);
		}
		registrationRepository.save(eventInfo);
		return EventInfoResponse.fromEntity(eventInfo);
	}

	private Registration fromRequest(RegistrationAddRequest registrationAddRequest){
		return Registration.builder()
				.username(registrationAddRequest.getUsername())
				.status(RegistrationStatus.WAITING)
				.build();
	}
	private EventInfo fromRequest(EventInfoAddRequest eventInfoAddRequest) {
		return EventInfo.builder()
				.id(eventInfoAddRequest.getId())
				.organizatorUsername(eventInfoAddRequest.getOrganizatorUsername())
				.userLimit(eventInfoAddRequest.getUserLimit())
				.users(eventInfoAddRequest.getUsers())
				.build();
	}



	public EventsInfoRestricted getEventsThatAUserRegistered(String username, boolean isClosedOption) {
		List<EventInfo> eventsInfo = mongoTemplate.find(
				Query.query(Criteria.where("users.username").is(username)),
				EventInfo.class);
		List<EventInfo> acceptedEventsInfo = new ArrayList<>();
		for(EventInfo eventInfoToken : eventsInfo){
			var registrationList = eventInfoToken.getUsers();
			for (Iterator<Registration> it = registrationList.iterator(); it.hasNext(); ) {
				Registration registration = it.next();
				if(registration.getUsername().equals(username) && registration.getStatus() == RegistrationStatus.ACCEPTED){
					acceptedEventsInfo.add(eventInfoToken);
				}

			}
		}
		List<String> eventUuids =  acceptedEventsInfo.stream()
				.map(EventInfo::getId)
				.toList();
		/*List<EventInfo> eventsInfo = mongoTemplate.find(
				Query.query(Criteria.where("users.username").is(username)
						.and("users.status").is(RegistrationStatus.ACCEPTED)),
				EventInfo.class);

		List<String> eventUuids =  eventsInfo.stream().filter(eventInfo -> eventInfo.getUsers())
				.map(EventInfo::getId)
				.toList();
*/

		final String closedEventsRequestPath = "/api/v1/event-management/get-closed-events/from-uuid-list";
		final String currentEventsRequestPath = "/api/v1/event-management/get-current-events/from-uuid-list";

		// get information of events inside the events that user has registered to
		var eventsInfoFiltered = webClientBuilder.build().post()
				.uri(uriBuilder -> uriBuilder
						.scheme("http")
						.host("event-management-service")
						.path(isClosedOption ? closedEventsRequestPath : currentEventsRequestPath)
						.build())
				.body(BodyInserters.fromValue(GetEventsRequest.builder()
						.idList(eventUuids)
						.build()))
				.retrieve()
				.bodyToMono(EventsInfoRestricted.class)
				.block();

		return eventsInfoFiltered;
	}


	public List<RegistrationInfoWithStatus> getEventsThatAUserMadeRegistrationRequest(String username) {
		// get all the events the user is registered to
		List<EventInfo> eventInfos = mongoTemplate.find(
				Query.query(Criteria.where("users.username").is(username)),
				EventInfo.class);

		List<String> uuids = eventInfos.stream().map(EventInfo::getId).toList();

		// get information of current events inside the events that user has registered to
		var currentEventsInfo = webClientBuilder.build().post()
				.uri(uriBuilder -> uriBuilder
						.scheme("http")
						.host("event-management-service")
						.path("/api/v1/event-management/get-current-events/from-uuid-list")
						.build())
				.body(BodyInserters.fromValue(GetEventsRequest.builder()
						.idList(uuids)
						.build()))
				.retrieve()
				.bodyToMono(EventsInfoRestricted.class)
				.block();

		var currentEventsResponseList = currentEventsInfo.getEvents();


		// obtain the registration status and event uuid's
		Map<String, RegistrationStatus> registrationStatusMap = new HashMap<>();
		for(EventInfo eventInfo : eventInfos){
			for(Registration registration : eventInfo.getUsers()){
				if(registration.getUsername().equals(username)){
					registrationStatusMap.put(eventInfo.getId(), registration.getStatus());
				}
			}
		}

        // merge two info
		List<RegistrationInfoWithStatus> registrationsWithStatus = new ArrayList<>();
		for(var currentEventResponse : currentEventsResponseList){
			var registrationInfoWithStatus = RegistrationInfoWithStatus.builder()
					.event(currentEventResponse)
					.registrationStatus(registrationStatusMap.get(currentEventResponse.getUuid()))
					.build();
			registrationsWithStatus.add(registrationInfoWithStatus);
		}


		return registrationsWithStatus;
	 }


	public int getRegistrationCountOfEvent(String eventUUID) {
		List<EventInfo> eventsInfo = mongoTemplate.find(
				Query.query(Criteria.where("id").is(eventUUID)
						.and("users.status").is(RegistrationStatus.WAITING)),
				EventInfo.class);
		if (eventsInfo.size() < 1){
			return 0;
		}
		var waitingUsers = eventsInfo.get(0).getUsers().stream().filter(user -> user.getStatus() == RegistrationStatus.WAITING).toList();

		return waitingUsers.size();
	}


	/*

	public List<RegistrationInfoWithStatus> getEventsThatAUserMadeRegistrationRequest(String username) {
		MatchOperation matchUser = Aggregation.match(Criteria.where("organizator_username").is(username));
		UnwindOperation unwindUsers = Aggregation.unwind("users");
		MatchOperation matchUsername = Aggregation.match(Criteria.where("users.username").is(username));
		ProjectionOperation projectFields = Aggregation.project("id", "users.status")
				.and("id").as("eventUUID")
				.and("users.status").as("registrationStatus");
		ReplaceRootOperation replaceRoot = Aggregation.replaceRoot().withValueOf("users");

		Aggregation aggregation = Aggregation.newAggregation(matchUser, unwindUsers, matchUsername, projectFields, replaceRoot);

		List<RegistrationInfoWithStatus> registrationResponses =
				mongoTemplate.aggregate(aggregation, EventInfo.class, RegistrationInfoWithStatus.class)
						.getMappedResults();
		return registrationResponses;
	}
*/


}
