package com.XPilot.XPilot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import com.XPilot.XPilot.models.media;
import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.models.Contratacion;
import com.XPilot.XPilot.repositories.mediaRepository;
import com.XPilot.XPilot.repositories.usuarioRepository;
import com.XPilot.XPilot.repositories.ContratacionRepository;
import com.XPilot.XPilot.repositories.MediaFotoRepository;
import com.XPilot.XPilot.services.NotificationService;
import com.XPilot.XPilot.services.NotificacionService;

@Controller
public class GalleryController {

    @Autowired private mediaRepository mediaRepo;
    @Autowired private usuarioRepository usuarioRepo;
    @Autowired private ContratacionRepository contratacionRepo;
    @Autowired private NotificationService notificationService;
    @Autowired private NotificacionService notificacionService;
    @Autowired private MediaFotoRepository mediaFotoRepo;

    @GetMapping("/gallery")
    public String gallery(Model model) {
        model.addAttribute("mediaList", mediaRepo.findAll());
        return "gallery";
    }

    @GetMapping("/artista/{id}")
    public String verArtista(@PathVariable Long id, Model model) {
        media artista = mediaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista no encontrado"));
        model.addAttribute("artista", artista);
        model.addAttribute("fotos", mediaFotoRepo.findByMedia(artista));
        return "artista-detalle";
    }

    @PostMapping("/contratar/{artistaId}")
    public String contratar(
            @PathVariable Long artistaId,
            @RequestParam(value = "obraId",     required = false) Long obraId,
            @RequestParam(value = "obraNombre", required = false) String obraNombre,
            @RequestParam(value = "fechaEvento", required = false) String fechaEvento,
            Principal principal) {

        if (principal == null) return "redirect:/login";

        usuario cliente = usuarioRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        media artista = mediaRepo.findById(artistaId)
                .orElseThrow(() -> new RuntimeException("Artista no encontrado"));

        // Verificar disponibilidad
        if (!artista.isDisponible()) {
            return "redirect:/artista/" + artistaId + "?ocupado=true";
        }

        // Marcar artista como no disponible
        artista.setDisponible(false);
        mediaRepo.save(artista);

        Contratacion c = new Contratacion();
        c.setCliente(cliente);
        c.setArtista(artista);
        c.setEstado("PENDIENTE");
        c.setNotificado(false);

        // ✅ Guardar datos de la obra si se contrató una específica
        if (obraId != null) c.setObraId(obraId);
        if (obraNombre != null && !obraNombre.isBlank()) c.setObraNombre(obraNombre);
        if (fechaEvento != null && !fechaEvento.isBlank()) c.setFechaEvento(java.time.LocalDate.parse(fechaEvento));
        // Guardar precio de la obra
        if (obraId != null && obraId > 0) {
            mediaFotoRepo.findById(obraId).ifPresent(foto -> c.setPrecioObra(foto.getPrecio()));
        } else {
            c.setPrecioObra(artista.getPrecio());
        }

        contratacionRepo.save(c);

        // ✅ Construir mensaje con detalle de obra
        String detalleObra = (obraNombre != null && !obraNombre.isBlank())
                ? " — obra: \"" + obraNombre + "\" (ID #" + obraId + ")"
                : "";

        try {
            usuario admin = usuarioRepo.findByRol("ADMIN");

            if (admin != null) {
                notificacionService.crearNotificacion(
                    admin,
                    "📋 " + cliente.getUname() + " contrató a " + artista.getArtist() + detalleObra
                );

                if (admin.getFcmToken() != null && !admin.getFcmToken().isBlank()) {
                    notificationService.enviarNotificacion(
                        admin.getFcmToken(),
                        "Nueva contratación",
                        cliente.getUname() + " contrató a " + artista.getArtist() + detalleObra
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error notificando admin: " + e.getMessage());
        }

        return "redirect:/mis-contratos";
    }
}
