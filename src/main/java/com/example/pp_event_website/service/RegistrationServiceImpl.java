package com.example.pp_event_website.service;

import com.example.pp_event_website.model.Event;
import com.example.pp_event_website.model.Registration;
import com.example.pp_event_website.model.User;
import com.example.pp_event_website.repository.EventRepository;
import com.example.pp_event_website.repository.RegistrationRepository;
import com.example.pp_event_website.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RegistrationServiceImpl implements RegistrationService{
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    @Override
    public List<Registration> getUserRegistrations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + userId + " не найден"));
        return registrationRepository.findByUserId(user);
    }

    @Override
    public List<Registration> getEventRegistrations(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Мероприятие с ID " + eventId + " не найдено"));
        return registrationRepository.findByEventId(event);
    }

    @Override
    public List<Registration> getRegistrationsByStatus(String status) {
        return registrationRepository.findByStatus(status);
    }

    @Override
    public Registration getById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Регистрация с ID " + id + " не найдена"));
    }

    @Override
    public long getTotalParticipants(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Мероприятие с ID " + eventId + " не найдено"));
        return registrationRepository.countByEventIdAndStatus(event, "confirmed");
    }

    @Override
    public boolean checkAvailableSlots(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Мероприятие с ID " + eventId + " не найдено"));
        long registeredCount = registrationRepository.countByEventIdAndStatus(event, "confirmed");
        return registeredCount < event.getMaxParticipants();
    }

    @Override
    public int getFreeSlots(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Мероприятие с ID " + eventId + " не найдено"));
        long registeredCount = registrationRepository.countByEventIdAndStatus(event, "confirmed");
        return event.getMaxParticipants() - (int) registeredCount;
    }

    @Override
    @Transactional
    public Registration registerUser(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + userId + " не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Мероприятие с ID " + eventId + " не найдено"));

        if (registrationRepository.findByUserIdAndEventId(user, event).isPresent()) {
            throw new IllegalStateException("Вы уже зарегистрированы на это мероприятие");
        }

        if (!checkAvailableSlots(eventId)) {
            throw new IllegalStateException("На мероприятие нет свободных мест");
        }

        Registration registration = new Registration();
        registration.setUserId(user);
        registration.setEventId(event);
        registration.setStatus("confirmed");

        return registrationRepository.save(registration);
    }

    @Override
    @Transactional
    public Registration updateStatus(Long id, String status) {
        Registration registration = getById(id);
        registration.setStatus(status);
        return registrationRepository.save(registration);
    }

    @Override
    @Transactional
    public void cancelRegistration(Long registrationId) {
        Registration registration = getById(registrationId);
        registration.setStatus("cancelled");
        registrationRepository.save(registration);
    }

    @Override
    @Transactional
    public void deleteRegistration(Long id) {
        if (!registrationRepository.existsById(id)) {
            throw new EntityNotFoundException("Нельзя удалить: регистрация с ID " + id + " не найдена");
        }
        registrationRepository.deleteById(id);
    }

    @Override
    public Page<Registration> searchByParameters(Long userId, Long eventId, String status, Pageable pageable) {
        return registrationRepository.searchByParameters(userId, eventId, (status != null && !status.isBlank()) ? status.trim() : null, pageable);
    }

    @Override
    @Transactional
    public Registration createRegistration(Long userId, Long eventId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Мероприятие с ID " + eventId + " не найдено"));

        if (registrationRepository.findByUserIdAndEventId(user, event).isPresent()) {
            throw new IllegalStateException("Регистрация этого пользователя на это событие уже существует");
        }

        Registration r = new Registration();
        r.setUserId(user);
        r.setEventId(event);
        r.setStatus((status == null || status.isBlank()) ? "confirmed" : status.trim());
        return registrationRepository.save(r);
    }

    @Override
    @Transactional
    public Registration updateRegistration(Long id, Long userId, Long eventId, String status) {
        Registration existing = getById(id);
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + userId + " не найден"));
            existing.setUserId(user);
        }
        if (eventId != null) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new EntityNotFoundException("Мероприятие с ID " + eventId + " не найдено"));
            existing.setEventId(event);
        }
        if (status != null && !status.isBlank()) {
            existing.setStatus(status.trim());
        }
        return registrationRepository.save(existing);
    }
}