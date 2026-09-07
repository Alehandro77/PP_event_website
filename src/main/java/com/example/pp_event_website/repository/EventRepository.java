package com.example.pp_event_website.repository;

import com.example.pp_event_website.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    //Для поиска без учета регистра
    List<Event> findByTitleContainingIgnoreCase(String title);
    List<Event> findByDescriptionContainingIgnoreCase(String description);
    List<Event> findByLocationContainingIgnoreCase(String location);

    List<Event> findByCategory(String category);
    List<Event> findByEventDate(LocalDate eventDate);
    List<Event> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
}