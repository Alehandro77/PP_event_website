package com.example.pp_event_website.controller.admin;

import com.example.pp_event_website.model.Registration;
import com.example.pp_event_website.service.EventService;
import com.example.pp_event_website.service.RegistrationService;
import com.example.pp_event_website.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/registrations")
public class AdminRegistrationController {

    private final RegistrationService registrationService;
    private final UserService userService;
    private final EventService eventService;

    public AdminRegistrationController(RegistrationService registrationService,
                                       UserService userService,
                                       EventService eventService) {
        this.registrationService = registrationService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 5, sort = "id") Pageable pageable,
            Model model) {

        Page<Registration> page = registrationService.searchByParameters(userId, eventId, status, pageable);

        model.addAttribute("registrationsPage", page);
        model.addAttribute("paramUserId", userId);
        model.addAttribute("paramEventId", eventId);
        model.addAttribute("paramStatus", status);

        // Списки для селектов — чтобы в шаблоне тянулись значения, а не голые id
        model.addAttribute("allUsers", userService.findAll());
        model.addAttribute("allEvents", eventService.findAll());

        return "admin/registrationList";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long userId,
                      @RequestParam Long eventId,
                      @RequestParam(required = false) String status,
                      RedirectAttributes redirectAttributes) {
        try {
            registrationService.createRegistration(userId, eventId, status);
            redirectAttributes.addFlashAttribute("successMessage", "Регистрация создана!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/registrations";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long id,
                         @RequestParam(required = false) Long userId,
                         @RequestParam(required = false) Long eventId,
                         @RequestParam(required = false) String status,
                         RedirectAttributes redirectAttributes) {
        try {
            registrationService.updateRegistration(id, userId, eventId, status);
            redirectAttributes.addFlashAttribute("successMessage", "Регистрация обновлена!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/registrations";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            registrationService.deleteRegistration(id);
            redirectAttributes.addFlashAttribute("successMessage", "Регистрация удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/registrations";
    }
}