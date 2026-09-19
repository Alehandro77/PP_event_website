package com.example.pp_event_website.controller.admin;

import com.example.pp_event_website.model.Role;
import com.example.pp_event_website.model.User;
import com.example.pp_event_website.repository.UserRepository;
import com.example.pp_event_website.service.EventService;
import com.example.pp_event_website.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final EventService eventService;
    private final UserService userService;
    private final UserRepository userRepository;

    public AdminController(EventService eventService, UserService userService, UserRepository userRepository) {
        this.eventService = eventService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/admin/")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        long totalAdmins = userService.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMIN)
                .count();

        if (userDetails != null) {
            User currentUser = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }

        model.addAttribute("totalUsers", userService.findAll().size());
        model.addAttribute("totalEvents", eventService.findAll().size());
        model.addAttribute("totalAdmins", totalAdmins);
        return "/admin/index";
    }
}
