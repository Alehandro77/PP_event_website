package com.example.pp_event_website.service;

import com.example.pp_event_website.model.Event;
import com.example.pp_event_website.model.User;
import com.example.pp_event_website.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Мероприятие с ID " + id + " не найдено"));
    }

    @Override
    public List<Event> findByCategory(String category) {
        return eventRepository.findByCategory(category);
    }

    @Override
    public List<Event> searchByTitle(String title) {
        return eventRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Event> findByLocation(String location) {
        return eventRepository.findByLocationContainingIgnoreCase(location);
    }

    @Override
    @Transactional
    public Event createEvent(Event event) {
        if (!eventRepository.findByTitleContainingIgnoreCase(event.getTitle()).isEmpty()) {
            throw new IllegalArgumentException("Мероприятие с таким названием уже существует");
        }
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event updateEvent(Long id, Event eventDetails) {
        Event existingEvent = findById(id);

        existingEvent.setTitle(eventDetails.getTitle());
        existingEvent.setDescription(eventDetails.getDescription());
        existingEvent.setCategory(eventDetails.getCategory());
        existingEvent.setEventDate(eventDetails.getEventDate());
        existingEvent.setEventTime(eventDetails.getEventTime());
        existingEvent.setLocation(eventDetails.getLocation());
        existingEvent.setMaxParticipants(eventDetails.getMaxParticipants());

        return eventRepository.save(existingEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("Нельзя удалить: мероприятие с ID " + id + " не найдено");
        }
        eventRepository.deleteById(id);
    }
}