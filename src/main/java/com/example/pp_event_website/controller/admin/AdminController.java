package com.example.pp_event_website.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/")
    public String home(Model model) {
        return "/admin/index";
    }
}
