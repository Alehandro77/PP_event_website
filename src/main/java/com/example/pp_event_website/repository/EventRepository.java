package com.example.pp_event_website.repository;

import com.example.pp_event_website.model.Event;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    //Для поиска без учета регистра
    List<Event> findByTitleContainingIgnoreCase(String title);
    List<Event> findByDescriptionContainingIgnoreCase(String description);
    List<Event> findByLocationContainingIgnoreCase(String location);

    //Встроенные для поиска
    List<Event> findByTitle(String Title);
    List<Event> findByCategory(String category);
    List<Event> findByEventDate(LocalDate eventDate);
    List<Event> findByEventTime(LocalTime eventTime);
    List<Event> findByLocation(String location);
    List<Event> findByMaxParticipants(Integer maxParticipants);
    List<Event> findByCreatedAt(LocalDateTime createdAt);

    /*Встроенные методы для пагинации - не будут использоваться, выполнен переход на единный SQL-запрос,
    как более экономное решение*/
    Page<Event> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Event> findByDescriptionIgnoreCase(String description, Pageable pageable);
    Page<Event> findByCategory(String category, Pageable pageable);
    Page<Event> findByEventDate(LocalDate eventDate, Pageable pageable);
    Page<Event> findByEventTime(LocalTime eventTime, Pageable pageable);
    Page<Event> findByLocationIgnoreCase(String location, Pageable pageable);
    Page<Event> findByMaxParticipants(Integer maxParticipants, Pageable pageable);
    Page<Event> findByCreatedAt(LocalDateTime createdAt, Pageable pageable);

    //SQL-запросы на фильтрацию и поиск данных
    @Query("SELECT e FROM Event e WHERE " +
            "(CAST(:title AS string) IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', CAST(:title AS string), '%'))) AND " +
            "(CAST(:description AS string) IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', CAST(:description AS string), '%'))) AND " +
            "(CAST(:category AS string) IS NULL OR LOWER(e.category) LIKE LOWER(CONCAT('%', CAST(:category AS string), '%'))) AND " +
            "(CAST(:eventDate AS date) IS NULL OR e.eventDate = :eventDate) AND " +
            "(CAST(:eventTime AS time) IS NULL OR e.eventTime = :eventTime) AND " +
            "(CAST(:location AS string) IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', CAST(:location AS string), '%'))) AND " +
            "(CAST(:maxParticipants AS integer) IS NULL OR e.maxParticipants >= :maxParticipants)")
    Page<Event> findByEventParameters(
            @Param("title") String title,
            @Param("description") String description,
            @Param("category") String category,
            @Param("eventDate") LocalDate eventDate,
            @Param("eventTime") LocalTime eventTime,
            @Param("location") String location,
            @Param("maxParticipants") Integer maxParticipants,
            Pageable pageable
    );

    //SQL-запрос на изменение данных
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Event e SET " +
            "e.title = COALESCE(:title, e.title), " +
            "e.description = COALESCE(:description, e.description), " +
            "e.category = COALESCE(:category, e.category), " +
            "e.eventDate = COALESCE(:eventDate, e.eventDate), " +
            "e.eventTime = COALESCE(:eventTime, e.eventTime), " +
            "e.location = COALESCE(:location, e.location), " +
            "e.maxParticipants = COALESCE(:maxParticipants, e.maxParticipants) " +
            "WHERE e.id = :id")
    int updateEventPartial(
            @Param("id") Long id,
            @Param("title") String title,
            @Param("description") String description,
            @Param("category") String category,
            @Param("eventDate") LocalDate eventDate,
            @Param("eventTime") LocalTime eventTime,
            @Param("location") String location,
            @Param("maxParticipants") Integer maxParticipants
    );
}