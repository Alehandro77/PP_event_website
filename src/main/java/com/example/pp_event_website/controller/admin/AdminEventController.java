package com.example.pp_event_website.controller.admin;

import com.example.pp_event_website.model.Event;
import com.example.pp_event_website.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String filterByEventParameters(
                                @RequestParam(required = false) String title,
                                @RequestParam(required = false) String description,
                                @RequestParam(required = false) String category,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate eventDate,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime eventTime,
                                @RequestParam(required = false) String location,
                                @RequestParam(required = false) Integer maxParticipants,
                                @PageableDefault(size = 3, sort = "id") Pageable pageable,
                                Model model) {

        Page<Event> eventPage = eventService.searchByEventParameters(title, description, category, eventDate,
                eventTime, location, maxParticipants, pageable);

        model.addAttribute("eventsPage", eventPage);

        model.addAttribute("paramTitle", title);
        model.addAttribute("paramDescription", description);
        model.addAttribute("paramCategory", category);
        model.addAttribute("paramEventDate", eventDate);
        model.addAttribute("paramEventTime", eventTime);
        model.addAttribute("paramLocation", location);
        model.addAttribute("paramMaxParticipants", maxParticipants);

        if (!model.containsAttribute("eventModel")) {
            model.addAttribute("eventModel", new Event());
        }

        return "admin/eventList";
    }



    @PostMapping("/add")
    public String addEvent(
            @Valid @ModelAttribute("eventModel") Event eventModel,
            BindingResult bindingResult,
            Model model,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) LocalDate eventDate,
            @RequestParam(required = false) LocalTime eventTime,
            @RequestParam(required = false) Integer maxParticipants,
            Pageable pageable
            ) {

        if (bindingResult.hasErrors()) {
            Page<Event> eventsPage = eventService.searchByEventParameters(
                    title, description, category, eventDate, eventTime, location, maxParticipants, pageable
            );
            model.addAttribute("eventsPage", eventsPage);

            model.addAttribute("paramTitle", title);
            model.addAttribute("paramDescription", description);
            model.addAttribute("paramCategory", category);
            model.addAttribute("paramLocation", location);
            model.addAttribute("paramEventDate", eventDate);
            model.addAttribute("paramEventTime", eventTime);
            model.addAttribute("paramMaxParticipants", maxParticipants);

            return "admin/eventList";
        }

        eventService.createEvent(eventModel);
        return "redirect:/admin/events";
    }

    @PostMapping("/update")
    public String updateEvent(@ModelAttribute Event eventModel, RedirectAttributes redirectAttributes) {
        try {
            eventService.updateEvent(eventModel.getId(), eventModel);
            redirectAttributes.addFlashAttribute("successMessage", "Событие успешно обновлено!");
        }

        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/events";
    }

    @PostMapping("/delete")
    public String deleteEvent(@RequestParam Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin/events";
    }
}
