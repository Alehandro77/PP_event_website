package com.example.pp_event_website.controller.user;

import com.example.pp_event_website.model.Event;
import com.example.pp_event_website.model.Registration;
import com.example.pp_event_website.model.User;
import com.example.pp_event_website.repository.EventRepository;
import com.example.pp_event_website.repository.RegistrationRepository;
import com.example.pp_event_website.repository.UserRepository;
import com.example.pp_event_website.service.RegistrationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final RegistrationService registrationService;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public ProfileController(UserRepository userRepository, RegistrationService registrationService, EventRepository eventRepository, RegistrationRepository registrationRepository) {
        this.userRepository = userRepository;
        this.registrationService = registrationService;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @GetMapping
    public String profile(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        List<Registration> registrations = registrationService.getUserRegistrations(currentUser.getId());

        List<Event> events = registrations.stream()
                .filter(r -> !"cancelled".equalsIgnoreCase(r.getStatus()))
                .map(Registration::getEvent)
                .toList();

        model.addAttribute("user", currentUser);
        model.addAttribute("events", events);
        return "public/profile";
    }

    @PostMapping("/deleteReg")
    public String deleteReg(@RequestParam Long eventId,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {

        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Мероприятие не найдено"));

        Registration registration = registrationRepository.findByUserAndEvent(currentUser, event)
                .orElseThrow(() -> new EntityNotFoundException("Регистрация не найдена"));

        registrationService.cancelRegistration(registration.getId());

        redirectAttributes.addFlashAttribute("successMessage", "Запись отменена");
        return "redirect:/user/profile";
    }

}
