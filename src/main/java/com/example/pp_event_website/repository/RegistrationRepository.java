package com.example.pp_event_website.repository;

import com.example.pp_event_website.model.Event;
import com.example.pp_event_website.model.Registration;
import com.example.pp_event_website.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUserId(User user);
    List<Registration> findByEventId(Event event);
    List<Registration> findByStatus(String status);
    long countByEventIdAndStatus(Event event, String status);
    Optional<Registration> findByUserIdAndEventId(User user, Event event);
}