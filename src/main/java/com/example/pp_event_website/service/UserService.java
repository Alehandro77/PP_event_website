package com.example.pp_event_website.service;
import com.example.pp_event_website.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    public List<User> findAll();
    public User findById(Long id);
    public User findByEmail(String email);

    //Поиск с помощью sql-запроса
    Page<User> searchByUserParameters(String name, String email, String role, Pageable pageable);

    public User createUser(User user);
    public User updateUser(Long id, User userDetails);
    public void deleteUser(Long id);


}
