package com.example.pp_event_website.service;

import com.example.pp_event_website.model.Event;

import java.util.List;

public interface EventService {
    List<Event> findAll();
    Event findById(Long id);
    List<Event> findByCategory(String category);
    List<Event> searchByTitle(String title);
    List<Event> findByLocation(String location);
    Event createEvent(Event event);
    Event updateEvent(Long id, Event eventDetails);
    void deleteEvent(Long id);
}
