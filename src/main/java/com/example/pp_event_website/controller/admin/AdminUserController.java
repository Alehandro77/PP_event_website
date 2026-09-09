package com.example.pp_event_website.controller.admin;

import com.example.pp_event_website.model.Role;
import com.example.pp_event_website.model.User;
import com.example.pp_event_website.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @PageableDefault(size = 5, sort = "id") Pageable pageable,
            Model model) {

        Page<User> usersPage = userService.searchByUserParameters(name, email, role, pageable);

        model.addAttribute("usersPage", usersPage);
        model.addAttribute("paramName", name);
        model.addAttribute("paramEmail", email);
        model.addAttribute("paramRole", role);
        model.addAttribute("roles", Role.values());

        if (!model.containsAttribute("userModel")) {
            model.addAttribute("userModel", new User());
        }
        return "admin/userList";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("userModel") User userModel,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Проверьте корректность полей");
            return "redirect:/admin/users";
        }
        try {
            userService.createUser(userModel);
            redirectAttributes.addFlashAttribute("successMessage", "Пользователь создан!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute User userModel, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(userModel.getId(), userModel);
            redirectAttributes.addFlashAttribute("successMessage", "Пользователь обновлён!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Пользователь удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}