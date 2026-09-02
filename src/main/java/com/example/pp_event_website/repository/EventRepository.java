package com.example.pp_event_website.repository;

import com.example.pp_event_website.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCategory(String category);
    List<Event> findByTitleContainingIgnoreCase(String title);
}