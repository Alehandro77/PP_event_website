package com.example.pp_event_website.service;
import com.example.pp_event_website.model.User;

import java.util.List;

public interface UserService {

    public List<User> findAll();

    public User findById(Long id);

    public User findByEmail(String email);

    public User createUser(User user);

    public User updateUser(User user);

    public void deleteUser(Long id);

}
