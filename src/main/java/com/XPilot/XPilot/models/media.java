package com.XPilot.XPilot.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 🔥 CAMBIO CLAVE

    private String artist;
    private String style;
    private String tags;
    private String urlm;
    private LocalDate datem;

    @Column(length = 1000)
    private String biografia;

    private Double precio;

    private boolean disponible = true;
    @Transient
    private Long totalContratos = 0L;
    @Transient
    private Double totalGanancias = 0.0;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private usuario usuario;

    // ================= GETTERS =================

    public Long getId() { return id; }

    public String getArtist() { return artist; }

    public String getStyle() { return style; }

    public String getTags() { return tags; }

    public String getUrlm() { return urlm; }

    public LocalDate getDatem() { return datem; }

    public String getBiografia() { return biografia; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public usuario getUsuario() { return usuario; }

    // ================= SETTERS =================

    public void setId(Long id) { this.id = id; }

    public void setArtist(String artist) { this.artist = artist; }

    public void setStyle(String style) { this.style = style; }

    public void setTags(String tags) { this.tags = tags; }

    public void setUrlm(String urlm) { this.urlm = urlm; }

    public void setDatem(LocalDate datem) { this.datem = datem; }

    public void setBiografia(String biografia) { this.biografia = biografia; }

    public void setUsuario(usuario usuario) { this.usuario = usuario; }
}