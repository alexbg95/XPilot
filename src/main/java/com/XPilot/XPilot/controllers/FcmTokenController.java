package com.XPilot.XPilot.controllers;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.usuarioRepository;

@RestController
public class FcmTokenController {

    @Autowired
    private usuarioRepository usuarioRepo;

    @PostMapping("/guardar-fcm-token")
    public ResponseEntity<String> guardarToken(@RequestBody Map<String, String> body,
                                               Authentication auth,
                                               Principal principal) {
        try {

            // 🔍 DEBUG (te ayudará a ver qué pasa realmente)
            System.out.println("Auth: " + auth);
            System.out.println("Principal: " + principal);

            // 🔐 Validación fuerte de autenticación
            if (auth == null || !auth.isAuthenticated() || principal == null) {
                System.out.println("❌ Usuario no autenticado");
                return ResponseEntity.status(401).body("NO AUTH");
            }

            String username = principal.getName();
            System.out.println("👤 Usuario autenticado: " + username);

            // 📦 Token
            String token = body.get("token");
            if (token == null || token.isBlank()) {
                return ResponseEntity.badRequest().body("TOKEN VACIO");
            }

            // 🔎 Buscar usuario
            usuario user = usuarioRepo.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 💾 Guardar token
            user.setFcmToken(token);
            usuarioRepo.save(user);

            System.out.println("🔥 Token guardado para: " + user.getEmail() + " [" + user.getRol() + "]");

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            System.out.println("❌ Error guardando token: " + e.getMessage());
            return ResponseEntity.status(500).body("ERROR");
        }
    }
}