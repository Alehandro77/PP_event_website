package com.example.pp_event_website.service;

import com.example.pp_event_website.model.Registration;

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
    Registration registerUser(Long userId, Long eventId);
    Registration updateStatus(Long id, String status);
    void cancelRegistration(Long registrationId);
    void deleteRegistration(Long id);
}
