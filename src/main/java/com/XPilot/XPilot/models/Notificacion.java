package com.XPilot.XPilot.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    // ✅ CORREGIDO: nombre de columna coincide con la BD (leida)
    @Column(name = "leida", columnDefinition = "TINYINT(1)")
    private boolean leido = false;

    private LocalDateTime fecha = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private usuario usuario;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public boolean isLeido() { return leido; }
    public void setLeido(boolean leido) { this.leido = leido; }

    public LocalDateTime getFecha() { return fecha; }

    public usuario getUsuario() { return usuario; }
    public void setUsuario(usuario usuario) { this.usuario = usuario; }
}
