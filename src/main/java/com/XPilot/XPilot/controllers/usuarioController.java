package com.XPilot.XPilot.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.services.usuarioServicesManager;
import com.XPilot.XPilot.repositories.usuarioRepository;

@RestController
@RequestMapping("/api/usuario")
public class usuarioController {

    @Autowired
    private usuarioServicesManager servicesManager;

    @Autowired
    private usuarioRepository usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    // 🔥 REGISTRO
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody usuario usuario) {

        Locale locale = LocaleContextHolder.getLocale();

        if (usuarioRepo.existsByUname(usuario.getUname())) {
            String msg = messageSource.getMessage("register.user.exists", null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        usuario.setPassw(passwordEncoder.encode(usuario.getPassw()));
        servicesManager.save(usuario);

        String msg = messageSource.getMessage("register.user.success", null, locale);
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    // 🔥 CRUD
    @GetMapping
    @Transactional(readOnly = true)
    public List<usuario> getAllUsuarios() {
        return servicesManager.all();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public usuario findUsuarioByID(@PathVariable long id) {
        return servicesManager.find(id);
    }

    @PutMapping("/{id}")
    public usuario updateUsuario(@PathVariable long id, @RequestBody usuario usuario) {
        if (usuario.getPassw() != null) {
            usuario.setPassw(passwordEncoder.encode(usuario.getPassw()));
        }
        return servicesManager.update(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable long id) {
        servicesManager.delete(id);
    }

    // 🔔 GUARDAR TOKEN FCM
    @PostMapping("/guardar-token")
    public ResponseEntity<String> guardarTokenUsuario(@RequestBody Map<String, String> body,
                                                       Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NO AUTH");
            }

            String token = body.get("token");
            if (token == null || token.isBlank()) {
                return ResponseEntity.badRequest().body("TOKEN VACIO");
            }

            usuario user = usuarioRepo.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            user.setFcmToken(token);
            usuarioRepo.save(user);

            System.out.println("🔥 Token FCM guardado para usuario: " + user.getEmail());
            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            System.out.println("❌ Error guardando token usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR");
        }
    }
}
