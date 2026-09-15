package com.example.pp_event_website.controller.user;

import com.example.pp_event_website.model.User;
import com.example.pp_event_website.repository.UserRepository;
import com.example.pp_event_website.service.EventService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EventService eventService;
    private final UserRepository userRepository;

    public HomeController(EventService eventService, UserRepository userRepository) {
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            User currentUser = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("user", currentUser);
        }

        model.addAttribute("events", eventService.findAll());
        return "/public/index";
    }
}