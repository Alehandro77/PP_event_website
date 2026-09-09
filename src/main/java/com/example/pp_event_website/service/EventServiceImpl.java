package com.example.pp_event_website.service;

import com.example.pp_event_website.model.Event;
import com.example.pp_event_website.model.User;
import com.example.pp_event_website.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    //Поиск всех значений
    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Мероприятие с ID " + id + " не найдено"));
    }

    //Поиск с учетом пагинации по отдельности
    @Override
    public Page<Event> searchByTitle(String title, Pageable pageable) {
        return eventRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Override
    public Page<Event> searchByDescription(String description, Pageable pageable) {
        return eventRepository.findByDescriptionIgnoreCase(description, pageable);
    }

    @Override
    public Page<Event> searchByCategory(String category, Pageable pageable) {
        return eventRepository.findByCategory(category, pageable);
    }

    @Override
    public Page<Event> searchByEventDate(LocalDate eventDate, Pageable pageable) {
        return eventRepository.findByEventDate(eventDate, pageable);
    }

    @Override
    public Page<Event> searchByEventTime(LocalTime eventTime, Pageable pageable) {
        return eventRepository.findByEventTime(eventTime, pageable);
    }

    @Override
    public Page<Event> searchByLocation(String location, Pageable pageable) {
        return eventRepository.findByLocationIgnoreCase(location, pageable);
    }

    @Override
    public Page<Event> searchByMaxParticipants(Integer max_participants, Pageable pageable) {
        return eventRepository.findByMaxParticipants(max_participants, pageable);
    }

    @Override
    public Page<Event> searchByCreatedAt(LocalDateTime created_at, Pageable pageable) {
        return eventRepository.findByCreatedAt(created_at, pageable);
    }

    //Методы без пагинации по отдельности

    @Override
    public List<Event> findByTitle(String title) {
        return eventRepository.findByTitle(title);
    }

    @Override
    public List<Event> findByDescription(String description) {
        return eventRepository.findByDescriptionContainingIgnoreCase(description);
    }

    @Override
    public List<Event> findByCategory(String category) {
        return eventRepository.findByCategory(category);
    }

    @Override
    public List<Event> findByEventDate(LocalDate eventDate) {
        return eventRepository.findByEventDate(eventDate);
    }

    @Override
    public List<Event> findByEventTime(LocalTime eventTime) {
        return eventRepository.findByEventTime(eventTime);
    }

    @Override
    public List<Event> findByLocation(String location) {
        return eventRepository.findByLocationContainingIgnoreCase(location);
    }

    @Override
    public List<Event> findByMaxParticipants(Integer max_participants) {
        return eventRepository.findByMaxParticipants(max_participants);
    }

    @Override
    public List<Event> findByCreatedAt(LocalDateTime created_at) {
        return eventRepository.findByCreatedAt(created_at);
    }

    //Поиск по sql-запросу
    @Override
    public Page<Event> searchByEventParameters(String title, String description, String category,
                                               LocalDate eventDate, LocalTime eventTime, String location,
                                               Integer max_participants, Pageable pageable) {
        String cleanTitle = (title != null && !title.isBlank() ? title.trim() : null);
        String cleanDescription = (description != null && !description.isBlank() ? description.trim() : null);
        String cleanCategory = (category != null && !category.isBlank() ? category.trim() : null);
        String cleanLocation = (location != null && !location.isBlank() ? location.trim() : null);

        return eventRepository.findByEventParameters(
                cleanTitle,
                cleanDescription,
                cleanCategory,
                eventDate,
                eventTime,
                cleanLocation,
                max_participants,
                pageable
        );
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
        if (id == null) {
            throw new IllegalArgumentException("ID события не может быть пустым!");
        }

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Событие с ID " + id + " не найдено!"));

        if (eventDetails.getTitle() != null && !eventDetails.getTitle().isBlank()) {
            existingEvent.setTitle(eventDetails.getTitle().trim());
        }
        if (eventDetails.getDescription() != null && !eventDetails.getDescription().isBlank()) {
            existingEvent.setDescription(eventDetails.getDescription().trim());
        }
        if (eventDetails.getCategory() != null && !eventDetails.getCategory().isBlank()) {
            existingEvent.setCategory(eventDetails.getCategory().trim());
        }
        if (eventDetails.getLocation() != null && !eventDetails.getLocation().isBlank()) {
            existingEvent.setLocation(eventDetails.getLocation().trim());
        }
        if (eventDetails.getEventDate() != null) {
            existingEvent.setEventDate(eventDetails.getEventDate());
        }
        if (eventDetails.getEventTime() != null) {
            existingEvent.setEventTime(eventDetails.getEventTime());
        }
        if (eventDetails.getMaxParticipants() != null) {
            existingEvent.setMaxParticipants(eventDetails.getMaxParticipants());
        }

        // 3. Сохраняем обновленный объект
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