package com.XPilot.XPilot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import com.XPilot.XPilot.models.media;
import com.XPilot.XPilot.repositories.mediaRepository;

@Controller
public class UsuarioViewController {

    private final mediaRepository mediaRepository;

    // 🔥 INYECCIÓN POR CONSTRUCTOR (CORRECTO)
    public UsuarioViewController(mediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @GetMapping("/login-view")
    public String login() {
        return "login";
    }

    @GetMapping("/usuario/gallery")
    public String gallery(Model model) {

        // 🔥 TRAER DATOS REALES DE LA BD
        List<media> lista = mediaRepository.findAll();

        // 🔍 DEBUG (opcional)
        System.out.println("ARTISTAS: " + lista.size());

        model.addAttribute("mediaList", lista);

        return "gallery";
    }
}