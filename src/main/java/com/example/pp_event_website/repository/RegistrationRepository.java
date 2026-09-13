package com.example.pp_event_website.repository;

import com.example.pp_event_website.model.Event;
import com.example.pp_event_website.model.Registration;
import com.example.pp_event_website.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUser(User user);
    List<Registration> findByEvent(Event event);
    List<Registration> findByStatus(String status);
    long countByEventAndStatus(Event event, String status);
    Optional<Registration> findByUserAndEvent(User user, Event event);

    @Query("SELECT r FROM Registration r " +
            "JOIN FETCH r.user u " +
            "JOIN FETCH r.event e " +
            "WHERE " +
            "(CAST(:userId AS integer) IS NULL OR u.id = :userId) AND " +
            "(CAST(:eventId AS integer) IS NULL OR e.id = :eventId) AND " +
            "(CAST(:status AS string) IS NULL OR LOWER(r.status) LIKE LOWER(CONCAT('%', CAST(:status AS string), '%')))")
    Page<Registration> searchByParameters(
            @Param("userId") Long userId,
            @Param("eventId") Long eventId,
            @Param("status") String status,
            Pageable pageable);
}