package com.example.pp_event_website.service;

import com.example.pp_event_website.model.Event;
import com.example.pp_event_website.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));
    }

    public List<Event> searchEventsByTitle(String title) {
        return eventRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Event> filterEventsByCategory(String category) {
        return eventRepository.findByCategory(category);
    }

    public List<Event> filterEventsByDate(LocalDate date) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getEventDate().equals(date))
                .toList();
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event eventDetails) {
        Event event = getEventById(id);
        event.setTitle(eventDetails.getTitle());
        event.setDescription(eventDetails.getDescription());
        event.setCategory(eventDetails.getCategory());
        event.setEventDate(eventDetails.getEventDate());
        event.setEventTime(eventDetails.getEventTime());
        event.setLocation(eventDetails.getLocation());
        event.setMaxParticipants(eventDetails.getMaxParticipants());
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}