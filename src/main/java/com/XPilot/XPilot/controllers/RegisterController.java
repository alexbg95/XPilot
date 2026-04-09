package com.XPilot.XPilot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.usuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class RegisterController {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String mostrarRegistro() {
        return "register";
    }

    @PostMapping("/register")
    public String registrarUsuario(
            @RequestParam String email,
            @RequestParam String uname,
            @RequestParam String password,
            Model model
    ) {

        if (usuarioRepository.existsByEmail(email)) {
            model.addAttribute("error", "El correo ya existe");
            return "register";
        }

        if (usuarioRepository.existsByUname(uname)) {
            model.addAttribute("error", "El usuario ya existe");
            return "register";
        }

        usuario user = new usuario();
        user.setEmail(email.trim());
        user.setUname(uname.trim());
        user.setPassw(passwordEncoder.encode(password));

        usuarioRepository.save(user);

        return "redirect:/login";
    }
}