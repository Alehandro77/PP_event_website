package com.example.pp_event_website.repository;

import com.example.pp_event_website.model.Role;
import com.example.pp_event_website.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
    List<User> findByRole(Role role);

    //SQL-запрос
    @Query("SELECT u FROM User u WHERE " +
            "(CAST(:name AS string) IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) AND " +
            "(CAST(:email AS string) IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', CAST(:email AS string), '%'))) AND " +
            "(:role IS NULL OR u.role = :role)")
    Page<User> findByUserParameters(
            @Param("name") String name,
            @Param("email") String email,
            @Param("role") Role role,
            Pageable pageable
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u SET " +
            "u.name = COALESCE(:name, u.name), " +
            "u.email = COALESCE(:email, u.email), " +
            "u.role = COALESCE(:role, u.role) " +
            "WHERE u.id = :id")
    int updateUserPartial(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("email") String email,
            @Param("role") Role role
    );
}