package com.example.pp_event_website.controller.user;

import com.example.pp_event_website.model.User;
import com.example.pp_event_website.model.Role;
import com.example.pp_event_website.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public String getAllCategories(Model model,
                                   @RequestParam(required = false) Long id,
                                   @RequestParam(required = false) String email) {

        if (id != null) {
            User user = userService.findById(id);
            if (user != null) {
                model.addAttribute("users", List.of(user));
                model.addAttribute("searchMessage", "Найден юзер с ID: " + id);
            } else {
                model.addAttribute("users", userService.findAll());
                model.addAttribute("errorMessage", "Юзер с ID " + id + " не найден");
            }
        } else if (email != null) {
            User user = userService.findByEmail(email);
            if (user != null) {
                model.addAttribute("users", List.of(user));
                model.addAttribute("searchMessage", "Найден юзер с email: " + email);
            } else {
                model.addAttribute("users", userService.findAll());
                model.addAttribute("errorMessage", "Юзер с email " + email + " не найден");
            }
        } else {
            model.addAttribute("users", userService.findAll());
        }
        model.addAttribute("roles", Role.values());
        return "/public/userList";
    }

    @PostMapping("/user/add")
    public String createUser(@Valid  @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/public/userList";
        }
        userService.createUser(user);
        return "redirect:/user";
    }

    @PostMapping("/user/update")
    public String updateUser(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/public/1userList";
        }
        userService.updateUser(user.getId(), user);
        return "redirect:/user";
    }

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return "redirect:/user";
    }
}

