package com.XPilot.XPilot.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.usuarioRepository;

@ControllerAdvice
public class GlobalModelAdvice {

    @Autowired
    private usuarioRepository usuarioRepo;

    @ModelAttribute("uname")
    public String uname(Principal principal) {
        if (principal == null) return null;
        try {
            return usuarioRepo.findByEmail(principal.getName())
                    .map(usuario::getUname).orElse(null);
        } catch (Exception e) { return null; }
    }

    @ModelAttribute("fotoPerfil")
    public String fotoPerfil(Principal principal) {
        if (principal == null) return null;
        try {
            return usuarioRepo.findByEmail(principal.getName())
                    .map(usuario::getFotoPerfil).orElse(null);
        } catch (Exception e) { return null; }
    }
}
