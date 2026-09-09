package com.example.pp_event_website.service;

import com.example.pp_event_website.model.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RegistrationService {
    List<Registration> getAllRegistrations();
    List<Registration> getUserRegistrations(Long userId);
    List<Registration> getEventRegistrations(Long eventId);
    List<Registration> getRegistrationsByStatus(String status);
    Registration getById(Long id);
    long getTotalParticipants(Long eventId);
    boolean checkAvailableSlots(Long eventId);
    int getFreeSlots(Long eventId);
    Page<Registration> searchByParameters(Long userId, Long eventId, String status, Pageable pageable);
    Registration createRegistration(Long userId, Long eventId, String status);
    Registration updateRegistration(Long id, Long userId, Long eventId, String status);
    Registration registerUser(Long userId, Long eventId);
    Registration updateStatus(Long id, String status);
    void cancelRegistration(Long registrationId);
    void deleteRegistration(Long id);
}
