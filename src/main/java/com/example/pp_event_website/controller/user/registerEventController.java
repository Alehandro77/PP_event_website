package com.example.pp_event_website.controller.user;

import com.example.pp_event_website.model.Event;
import com.example.pp_event_website.repository.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("user/registerEventController")
public class registerEventController {

    private final EventRepository eventRepository;

    public registerEventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping("/reg")
    public String reg(Model model, @RequestParam Long id) {
        Event event = eventRepository.findById(id).get();
        model.addAttribute("event", event);
        return "public/registerEvent";
    }
}
