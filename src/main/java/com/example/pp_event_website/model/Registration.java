package com.example.pp_event_website.model;

import java.time.LocalDateTime;

public class Registration {
    private Long id;
    private Long userId;
    private Long eventId;
    private LocalDateTime registeredAt;
    private String status;

    public Registration() {}

    public Registration(Long id, Long userId, Long eventId, LocalDateTime registeredAt, String status) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.registeredAt = registeredAt;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}