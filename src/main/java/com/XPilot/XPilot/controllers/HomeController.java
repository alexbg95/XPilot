package com.XPilot.XPilot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.XPilot.XPilot.services.mediaServicesManager;

@Controller
public class HomeController {

    @Autowired
    private mediaServicesManager mediaService;

    @GetMapping("/")
    public String index(Model model) {
        // 1. Cargamos los artistas
        model.addAttribute("mediaList", mediaService.all());

        // 2. Buscamos la sesión manualmente para asegurar que llegue al HTML
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            model.addAttribute("usuarioLogueado", true);
            model.addAttribute("nombreUsuario", auth.getName());
        } else {
            model.addAttribute("usuarioLogueado", false);
        }

        return "index"; 
    }
}