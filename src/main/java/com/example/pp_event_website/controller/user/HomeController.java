package com.example.pp_event_website.controller.user;

import  com.example.pp_event_website.model.User;
import com.example.pp_event_website.repository.UserRepository;
import com.example.pp_event_website.service.EventService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final EventService eventService;
    private final UserRepository userRepository;

    public HomeController(EventService eventService, UserRepository userRepository) {
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            User currentUser = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("user", currentUser);
        }

        if ((keyword != null && !keyword.isBlank()) || (category != null && !category.isBlank())) {
            model.addAttribute("events", eventService.searchEvents(keyword, category));
        } else {
            model.addAttribute("events", eventService.findAll());
        }

        model.addAttribute("selectedCategory", category);
        model.addAttribute("searchKeyword", keyword);

        return "public/index";
    }

    @GetMapping("/public/events/{id}")
    public String getEventDetails(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            User currentUser = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("user", currentUser);
        }

        var event = eventService.findById(id);
        if (event == null) {
            return "redirect:/";
        }

        model.addAttribute("event", eventService.findById(id));
        return "public/event-detail";
    }
}