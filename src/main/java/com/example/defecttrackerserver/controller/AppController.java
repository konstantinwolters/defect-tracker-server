package com.example.defecttrackerserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AppController {

    @GetMapping
    public RedirectView index() {
        return new RedirectView("/swagger-ui.html");
    }
}
