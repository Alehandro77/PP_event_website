package com.example.pp_event_website.controller.user;

import com.example.pp_event_website.model.User;
import com.example.pp_event_website.repository.UserRepository;
import com.example.pp_event_website.service.EventService;
import com.example.pp_event_website.service.RegistrationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/events")
public class RegisterEventController {

    private final EventService eventService;
    private final RegistrationService registrationService;
    private final UserRepository userRepository;

    public RegisterEventController(EventService eventService,
                                   RegistrationService registrationService,
                                   UserRepository userRepository) {
        this.eventService = eventService;
        this.registrationService = registrationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/details")
    public String showDetails(Model model, @RequestParam Long id) {
        var event = eventService.findById(id);
        int freeSlots = registrationService.getFreeSlots(id);

        model.addAttribute("event", event);
        model.addAttribute("freeSlots", freeSlots);

        return "public/registerEvent";
    }

    @PostMapping("/{eventId}/register")
    public String registerForEvent(@PathVariable("eventId") Long eventId,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        try {
            registrationService.registerUser(currentUser.getId(), eventId);
            redirectAttributes.addFlashAttribute("successMessage", "Вы успешно зарегистрированы!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/public/events/" + eventId;
    }
}