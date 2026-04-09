package com.XPilot.XPilot.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 🔥 CAMBIO CLAVE

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String uname;

    @Column(nullable = false)
    private String passw;

    // 🔥 ROL
    @Column(nullable = false)
    private String rol = "USER";

    // 🔔 TOKEN FIREBASE
    @Column(name = "fcm_token", length = 500)
    private String fcmToken;

    // 🔗 RELACIÓN CON MEDIA
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("usuario")
    private List<media> media = new ArrayList<>();

    // 🔹 CONSTRUCTOR VACÍO
    public usuario() {}

    // 🔹 CONSTRUCTOR COMPLETO
    public usuario(Long id, String email, String uname, String passw, String rol) {
        this.id = id;
        this.email = email;
        this.uname = uname;
        this.passw = passw;
        this.rol = rol;
    }

    // ================= GETTERS Y SETTERS =================

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

    public String getFcmToken() { return fcmToken; }

    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }

    public List<media> getMedia() { return media; }

    public void setMedia(List<media> media) { this.media = media; }
}