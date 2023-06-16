package com.event.eventmanagementservice.repository;


import com.event.eventmanagementservice.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
	@Query(value = "select * from events where events.id in :ids", nativeQuery = true)
	List<Event> findAllByIds(@Param("ids") List<String> ids);

	@Query(value = "select * from events where events.organizator_username in :usernames", nativeQuery = true)
	Page<Event> findAllByUsernames(Pageable pageable, @Param("usernames") List<String> usernames);

	@Query(value = "select * from events where events.organizator_username = :username", nativeQuery = true)
	Page<Event> findByUsername(Pageable pageable,  @Param("username") String username);


	@Query(value = "SELECT * FROM events WHERE LOWER(events.name) LIKE LOWER(CONCAT('%', :searchKey, '%')) AND events.start_date > :currentDate", nativeQuery=true)
	Page<Event> findCurrentEventsBySearchKey(@Param("searchKey") String searchKey, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);

	List<Event> findByOrganizatorUsername(String organizatorUsername);

	List<Event> findByIdInAndStartDateAfter(List<String> ids, LocalDateTime startDate);

	List<Event> findByIdInAndStartDateBefore(List<String> ids, LocalDateTime endDate);

	List<Event> findByStartDateAfterOrderByStartDateAsc(LocalDateTime startDate);
	List<Event> findByStartDateBeforeOrderByStartDateAsc(LocalDateTime startDate);



}
