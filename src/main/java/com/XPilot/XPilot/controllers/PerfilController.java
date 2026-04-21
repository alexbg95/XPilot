package com.XPilot.XPilot.controllers;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.usuarioRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Controller
public class PerfilController {

    @Autowired private usuarioRepository usuarioRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private Cloudinary cloudinary;

    private usuario getUser(Principal principal) {
        if (principal == null) throw new RuntimeException("No autenticado");
        return usuarioRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // ── VER PERFIL ────────────────────────────────────────────────
    @GetMapping("/perfil")
    public String verPerfil(Model model, Principal principal) {
        model.addAttribute("usuario", getUser(principal));
        return "perfil";
    }

    // ── ACTUALIZAR NOMBRE ─────────────────────────────────────────
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

    // ── SUBIR FOTO DE PERFIL ──────────────────────────────────────
    @PostMapping("/perfil/foto")
    public String subirFoto(@RequestParam("foto") MultipartFile foto,
                            Principal principal, RedirectAttributes ra) {
        try {
            usuario user = getUser(principal);
            if (foto != null && !foto.isEmpty()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> resultado = cloudinary.uploader().upload(
                        foto.getBytes(),
                        ObjectUtils.asMap("folder", "xpilot/usuarios",
                                          "transformation", "w_200,h_200,c_fill,g_face")
                );
                user.setFotoPerfil((String) resultado.get("secure_url"));
                usuarioRepo.save(user);
                ra.addAttribute("ok", "foto");
            }
        } catch (Exception e) {
            System.out.println("❌ Error subiendo foto: " + e.getMessage());
            ra.addAttribute("error", "foto");
        }
        return "redirect:/perfil";
    }

    // ── ACTUALIZAR CONTRASEÑA ─────────────────────────────────────
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
