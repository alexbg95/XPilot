package com.XPilot.XPilot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.usuarioRepository;
import com.XPilot.XPilot.services.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.UUID;

@Controller
public class RegisterController {

    @Autowired private usuarioRepository usuarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EmailService emailService;

    @GetMapping("/register")
    public String mostrarRegistro() { return "register"; }

    @PostMapping("/register")
    public String registrarUsuario(
            @RequestParam String email,
            @RequestParam String uname,
            @RequestParam String password,
            Model model) {

        if (usuarioRepository.existsByEmail(email)) {
            model.addAttribute("error", "El correo ya existe");
            return "register";
        }
        if (usuarioRepository.existsByUname(uname)) {
            model.addAttribute("error", "El usuario ya existe");
            return "register";
        }

        String token = UUID.randomUUID().toString();
        usuario user = new usuario();
        user.setEmail(email.trim());
        user.setUname(uname.trim());
        user.setPassw(passwordEncoder.encode(password));
        user.setVerificado(false);
        user.setTokenVerificacion(token);
        usuarioRepository.save(user);

        emailService.enviarConfirmacion(email.trim(), token);

        return "redirect:/login-view?verificar=true";
    }

    @GetMapping("/verificar")
    public String verificarEmail(@RequestParam String token, Model model) {
        usuarioRepository.findByTokenVerificacion(token).ifPresentOrElse(user -> {
            user.setVerificado(true);
            user.setTokenVerificacion(null);
            usuarioRepository.save(user);
        }, () -> model.addAttribute("error", "Token inválido"));
        return "redirect:/login-view?verificado=true";
    }
}
