package com.XPilot.XPilot.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contrataciones")
public class Contratacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private usuario cliente;

    // ⚠️ artista es MEDIA
    @ManyToOne
    @JoinColumn(name = "artista_id")
    private media artista;

    private LocalDate fechaEvento;

    private String estado = "PENDIENTE";

    private boolean notificado = false;

    // ✅ Fecha y hora de cuando se envió la solicitud
    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud = LocalDateTime.now(java.time.ZoneId.of("America/Bogota"));

    // Obra específica contratada (opcional)
    private Long obraId;

    @Column(length = 200)
    private String obraNombre;

    private Double precioObra;

    public Long getId() { return id; }

    public usuario getCliente() { return cliente; }
    public void setCliente(usuario cliente) { this.cliente = cliente; }

    public media getArtista() { return artista; }
    public void setArtista(media artista) { this.artista = artista; }

    public LocalDate getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(LocalDate fechaEvento) { this.fechaEvento = fechaEvento; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isNotificado() { return notificado; }
    public void setNotificado(boolean notificado) { this.notificado = notificado; }

    public Long getObraId() { return obraId; }
    public void setObraId(Long obraId) { this.obraId = obraId; }

    public String getObraNombre() { return obraNombre; }
    public void setObraNombre(String obraNombre) { this.obraNombre = obraNombre; }
    public Double getPrecioObra() { return precioObra; }
    public void setPrecioObra(Double precioObra) { this.precioObra = precioObra; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
}