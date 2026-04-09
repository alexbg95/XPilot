package com.XPilot.XPilot.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.XPilot.XPilot.models.usuario;

public class CustomUserDetails implements UserDetails {

    private usuario user;

    public CustomUserDetails(usuario user) {
        this.user = user;
    }

    // 🔥 PARA USAR EL USUARIO EN CONTROLADORES
    public usuario getUsuario() {
        return user;
    }

    // 🔐 ROLES CORREGIDOS (SOLUCIÓN DEFINITIVA)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String rol = user.getRol();

        // 🔥 Evita duplicar ROLE_
        if (rol != null && !rol.startsWith("ROLE_")) {
            rol = "ROLE_" + rol;
        }

        return Collections.singletonList(
            new SimpleGrantedAuthority(rol)
        );
    }

    @Override
    public String getPassword() {
        return user.getPassw();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}