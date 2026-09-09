package com.example.pp_event_website.controller;

import com.example.pp_event_website.model.Event;
import com.example.pp_event_website.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String findAll(@PageableDefault(size = 3, sort = "id") Pageable pageable, Model model) {
        Page<Event> eventPage = eventService.findAll(pageable);
        model.addAttribute("eventsPage", eventPage);
        if (!model.containsAttribute("eventModel")) {
            model.addAttribute("eventModel", new Event());
        }
        return "admin/eventList";
    }

    @GetMapping("/filter")
    public String filterByTitle(@RequestParam(required = false)  String title,
                                @PageableDefault(size = 3, sort = "id") Pageable pageable,
                                Model model) {
        Page<Event> eventPage;

        if (title != null && !title.isBlank()) {
            eventPage = eventService.searchByTitle(title, pageable);
        }
        else {
            eventPage = eventService.findAll(pageable);
        }

        model.addAttribute("eventsPage", eventPage);
        model.addAttribute("paramValue", title);

        return "admin/eventList";
    }

    @PostMapping("/add")
    public String addEvent(
            @Valid @ModelAttribute("eventModel") Event eventModel,
            BindingResult bindingResult,
            @PageableDefault(size = 3) Pageable pageable,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("eventsPage", eventService.findAll(pageable));
            return "admin/eventList";
        }

        eventService.createEvent(eventModel);
        return "redirect:/admin/events";
    }

    @PostMapping("/update")
    public String updateEvent(@Valid @ModelAttribute Event eventModel) {
        eventService.updateEvent(eventModel.getId(), eventModel);
        return "redirect:/admin/events";
    }

    @PostMapping("/delete")
    public String deleteEvent(@RequestParam Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin/events";
    }
}
