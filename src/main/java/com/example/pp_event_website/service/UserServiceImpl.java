package com.example.pp_event_website.service;

import com.example.pp_event_website.model.Role;
import com.example.pp_event_website.model.User;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Page<User> searchByUserParameters(String name, String email, String role, Pageable pageable) {
        String cleanName = (name != null && !name.isBlank() ? name.trim() : null);
        String cleanEmail = (email != null && !email.isBlank() ? email.trim() : null);

        Role cleanRole = null;
        if (role != null && !role.isBlank()) {
            try {
                cleanRole = Role.valueOf(role.trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }

        return userRepository.findByUserParameters(
                cleanName,
                cleanEmail,
                cleanRole,
                pageable
        );
    }

    @Override
    @Transactional
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        if (user.getRegisteredAt() == null) {
            user.setRegisteredAt(LocalDateTime.now());
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User details) {
        if (id == null) {
            throw new IllegalArgumentException("ID пользователя не может быть пустым!");
        }
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));

        if (details.getName() != null && !details.getName().isBlank()) {
            existing.setName(details.getName().trim());
        }
        if (details.getEmail() != null && !details.getEmail().isBlank()) {
            existing.setEmail(details.getEmail().trim());
        }
        if (details.getPasswordHash() != null && !details.getPasswordHash().isBlank()) {
            existing.setPasswordHash(details.getPasswordHash());
        }
        if (details.getRole() != null) {
            existing.setRole(details.getRole());
        }
        return userRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Нельзя удалить: пользователь с ID " + id + " не найден");
        }
        userRepository.deleteById(id);
    }
}