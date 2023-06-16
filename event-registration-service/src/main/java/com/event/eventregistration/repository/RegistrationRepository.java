package com.event.eventregistration.repository;

import com.event.eventregistration.entity.EventInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends MongoRepository<EventInfo, String> {
}
