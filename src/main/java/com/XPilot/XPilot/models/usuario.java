package com.XPilot.XPilot.models;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuario")
public class usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String uname;

    @Column(nullable = false)
    private String passw;

    @Column(nullable = false)
    private String rol = "USER";

    @Column(name = "verificado")
    private boolean verificado = false;

    @Column(name = "token_verificacion", length = 100)
    private String tokenVerificacion;

    @Column(name = "fcm_token", length = 500)
    private String fcmToken;

    // ✅ FOTO DE PERFIL
    @Column(name = "foto_perfil", length = 500)
    private String fotoPerfil;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("usuario")
    @JsonIgnore
    private List<media> media = new ArrayList<>();

    public usuario() {}

    public usuario(Long id, String email, String uname, String passw, String rol) {
        this.id = id;
        this.email = email;
        this.uname = uname;
        this.passw = passw;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUname() { return uname; }
    public void setUname(String uname) { this.uname = uname; }
    public String getPassw() { return passw; }
    public void setPassw(String passw) { this.passw = passw; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public boolean isVerificado() { return verificado; }
    public void setVerificado(boolean verificado) { this.verificado = verificado; }
    public String getTokenVerificacion() { return tokenVerificacion; }
    public void setTokenVerificacion(String tokenVerificacion) { this.tokenVerificacion = tokenVerificacion; }
    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
    public List<media> getMedia() { return media; }
    public void setMedia(List<media> media) { this.media = media; }
}
