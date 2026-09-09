package com.example.pp_event_website.service;

import com.example.pp_event_website.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface EventService {
    //Поиск всех
    List<Event> findAll();
    Page<Event> findAll(Pageable pageable);
    //Поиск по параметрам сущностей
    Event findById(Long id);
    //Включая пагинацию
    Page<Event> searchByTitle(String title, Pageable pageable);
    Page<Event> searchByDescription(String description, Pageable pageable);
    Page<Event> searchByCategory(String category, Pageable pageable);
    Page<Event> searchByEventDate(LocalDate eventDate, Pageable pageable);
    Page<Event> searchByEventTime(LocalTime eventTime, Pageable pageable);
    Page<Event> searchByLocation(String location, Pageable pageable);
    Page<Event> searchByMaxParticipants(Integer maxParticipants, Pageable pageable);
    Page<Event> searchByCreatedAt(LocalDateTime createdAt, Pageable pageable);

    //Без учета пагинации
    List<Event> findByTitle(String title);
    List<Event> findByDescription(String description);
    List<Event> findByCategory(String category);
    List<Event> findByEventDate(LocalDate eventDate);
    List<Event> findByEventTime(LocalTime eventTime);
    List<Event> findByLocation(String location);
    List<Event> findByMaxParticipants(Integer maxParticipants);
    List<Event> findByCreatedAt(LocalDateTime createdAt);

    //Поиск с помощью sql-запроса
    Page<Event> searchByEventParameters(String title, String description, String category,
                                        LocalDate eventDate, LocalTime eventTime, String location,
                                        Integer maxParticipants, Pageable pageable);

    Event createEvent(Event event);
    Event updateEvent(Long id, Event eventDetails);
    void deleteEvent(Long id);
}
