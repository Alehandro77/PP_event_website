package com.example.pp_event_website.repository;

import com.example.pp_event_website.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //Встроенные для поиска
    List<Event> findByTitle(String Title);
    List<Event> findByCategory(String category);
    List<Event> findByEventDate(LocalDate eventDate);
    List<Event> findByEventTime(LocalTime eventTime);
    List<Event> findByLocation(String location);
    List<Event> findByMaxParticipants(Integer maxParticipants);
    List<Event> findByCreatedAt(LocalDateTime createdAt);

    //Встроенные методы для пагинации
    Page<Event> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Event> findByDescriptionIgnoreCase(String description, Pageable pageable);
    Page<Event> findByCategory(String category, Pageable pageable);
    Page<Event> findByEventDate(LocalDate eventDate, Pageable pageable);
    Page<Event> findByEventTime(LocalTime eventTime, Pageable pageable);
    Page<Event> findByLocationIgnoreCase(String location, Pageable pageable);
    Page<Event> findByMaxParticipants(Integer maxParticipants, Pageable pageable);
    Page<Event> findByCreatedAt(LocalDateTime createdAt, Pageable pageable);
}