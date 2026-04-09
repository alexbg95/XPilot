package com.XPilot.XPilot.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.usuarioRepository;

@Controller
public class DashboardController {

    @Autowired
    private usuarioRepository usuarioRepo;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {

        usuario user = usuarioRepo.findByEmail(principal.getName()).get();

        model.addAttribute("usuarioLogueado", user);

        return "dashboard";
    }
}