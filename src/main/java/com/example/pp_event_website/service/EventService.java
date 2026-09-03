package com.example.pp_event_website.service;

import com.example.pp_event_website.model.Event;

import java.util.List;

public interface EventService {

    public List<Event> findAll();

    public Event findById(Long id);

    public List<Event> findByCategory(String category);

    public Event createEvent(Event event);

    public Event updateEvent(Event event);

    public void deleteEvent(Long id);

}
