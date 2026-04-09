package com.XPilot.XPilot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.services.usuarioServicesManager;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private usuarioServicesManager service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public usuario register(@RequestBody usuario usr) {

        // Encriptar contraseña ANTES de guardar
        usr.setPassw(passwordEncoder.encode(usr.getPassw()));

        return service.save(usr);
    }
}
