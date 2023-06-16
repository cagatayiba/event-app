package com.event.userservice.repository;

import com.event.userservice.model.ApplicationUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<ApplicationUsers , Integer> {
    @Transactional
    Optional<ApplicationUsers> findByUsername(String username);
    @Transactional
    List<ApplicationUsers> findByUsernameIn(List<String> usernames);
    @Transactional
    List<ApplicationUsers> findByUsernameContainingIgnoreCase(String username);
}
