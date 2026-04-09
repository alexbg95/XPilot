package com.XPilot.XPilot.models;

import jakarta.persistence.*;

@Entity
@Table(name = "media_fotos")
public class MediaFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @Column(length = 200)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "media_id")
    private media media;

    public MediaFoto() {}
    public MediaFoto(String url, String nombre, media media) {
        this.url = url;
        this.nombre = nombre;
        this.media = media;
    }

    public Long getId() { return id; }
    public String getUrl() { return url; }
    public String getNombre() { return nombre; }
    public media getMedia() { return media; }

    public void setId(Long id) { this.id = id; }
    public void setUrl(String url) { this.url = url; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setMedia(media media) { this.media = media; }
}
