package com.XPilot.XPilot.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.usuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final usuarioRepository usuarioRepository;

    public CustomUserDetailsService(usuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        usuario user = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new CustomUserDetails(user); // 🔥 AQUÍ USA LA OTRA CLASE
    }
}