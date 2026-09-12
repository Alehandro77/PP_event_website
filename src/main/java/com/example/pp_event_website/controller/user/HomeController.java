package com.example.pp_event_website.controller.user;

import com.example.pp_event_website.repository.EventRepository;
import com.example.pp_event_website.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EventRepository eventRepository;

    public HomeController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("events", eventRepository.findAll());
        return "/public/index";
    }

}
