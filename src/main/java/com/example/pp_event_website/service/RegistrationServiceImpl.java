package com.example.pp_event_website.service;

import com.example.pp_event_website.model.Event;
import com.example.pp_event_website.model.Registration;
import com.example.pp_event_website.model.User;
import com.example.pp_event_website.repository.EventRepository;
import com.example.pp_event_website.repository.RegistrationRepository;
import com.example.pp_event_website.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationServiceImpl {

//    private final RegistrationRepository registrationRepository;
//    private final UserRepository userRepository;
//    private final EventRepository eventRepository;
//
//    public RegistrationServiceImpl(RegistrationRepository registrationRepository,
//                                   UserRepository userRepository,
//                                   EventRepository eventRepository) {
//        this.registrationRepository = registrationRepository;
//        this.userRepository = userRepository;
//        this.eventRepository = eventRepository;
//    }
//
//    public List<Registration> getAllRegistrations() {
//        return registrationRepository.findAll();
//    }
//
//    public List<Registration> getUserRegistrations(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
//        return registrationRepository.findByUser(user);
//    }
//
//    public List<Registration> getEventRegistrations(Long eventId) {
//        Event event = eventRepository.findById(eventId)
//                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));
//        return registrationRepository.findByEvent(event);
//    }
//
//    public long getTotalParticipants(Long eventId) {
//        Event event = eventRepository.findById(eventId)
//                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));
//        return registrationRepository.countByEventAndStatus(event, "confirmed");
//    }
//
//    public boolean checkAvailableSlots(Long eventId) {
//        Event event = eventRepository.findById(eventId)
//                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));
//        long registeredCount = registrationRepository.countByEventAndStatus(event, "confirmed");
//        return registeredCount < event.getMaxParticipants();
//    }
//
//    public int getFreeSlots(Long eventId) {
//        Event event = eventRepository.findById(eventId)
//                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));
//        long registeredCount = registrationRepository.countByEventAndStatus(event, "confirmed");
//        return event.getMaxParticipants() - (int) registeredCount;
//    }
//
//    @Transactional
//    public Registration registerUser(Long userId, Long eventId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
//
//        Event event = eventRepository.findById(eventId)
//                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));
//
//        if (registrationRepository.findByUserAndEvent(user, event).isPresent()) {
//            throw new RuntimeException("Вы уже зарегистрированы на это мероприятие");
//        }
//
//        if (!checkAvailableSlots(eventId)) {
//            throw new RuntimeException("Нет свободных мест");
//        }
//
//        Registration registration = new Registration();
//        registration.setUserId(user.getId());
//        registration.setEventId(event.getId());
//        registration.setStatus("confirmed");
//        registration.setRegisteredAt(LocalDateTime.now());
//
//        return registrationRepository.save(registration);
//    }
//
//    @Transactional
//    public void cancelRegistration(Long registrationId) {
//        Registration registration = registrationRepository.findById(registrationId)
//                .orElseThrow(() -> new RuntimeException("Регистрация не найдена"));
//        registration.setStatus("cancelled");
//        registrationRepository.save(registration);
//    }
//
//    @Transactional
//    public void deleteRegistration(Long id) {
//        registrationRepository.deleteById(id);
//    }
}