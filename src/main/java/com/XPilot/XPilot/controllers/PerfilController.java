package com.XPilot.XPilot.controllers;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.usuarioRepository;

@Controller
public class PerfilController {

    @Autowired private usuarioRepository usuarioRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    private usuario getUser(Principal principal) {
        if (principal == null) throw new RuntimeException("No autenticado");
        return usuarioRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @GetMapping("/perfil")
    public String verPerfil(Model model, Principal principal) {
        model.addAttribute("usuario", getUser(principal));
        return "perfil";
    }

    @PostMapping("/perfil/nombre")
    public String actualizarNombre(@RequestParam("uname") String uname,
                                   Principal principal, RedirectAttributes ra) {
        try {
            usuario user = getUser(principal);
            if (uname != null && !uname.isBlank()) {
                user.setUname(uname.trim());
                usuarioRepo.save(user);
                ra.addAttribute("ok", "nombre");
            }
        } catch (Exception e) { ra.addAttribute("error", "true"); }
        return "redirect:/perfil";
    }

    @PostMapping("/perfil/password")
    public String actualizarPassword(@RequestParam("passwordActual") String passwordActual,
                                     @RequestParam("passwordNuevo") String passwordNuevo,
                                     @RequestParam("passwordConfirm") String passwordConfirm,
                                     Principal principal, RedirectAttributes ra) {
        try {
            usuario user = getUser(principal);
            if (!passwordEncoder.matches(passwordActual, user.getPassw())) {
                ra.addAttribute("error", "password"); return "redirect:/perfil";
            }
            if (!passwordNuevo.equals(passwordConfirm)) {
                ra.addAttribute("error", "confirm"); return "redirect:/perfil";
            }
            if (passwordNuevo.length() < 6) {
                ra.addAttribute("error", "corta"); return "redirect:/perfil";
            }
            user.setPassw(passwordEncoder.encode(passwordNuevo));
            usuarioRepo.save(user);
            ra.addAttribute("ok", "password");
        } catch (Exception e) { ra.addAttribute("error", "true"); }
        return "redirect:/perfil";
    }
}
