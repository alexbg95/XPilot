package com.XPilot.XPilot.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.XPilot.XPilot.models.Contratacion;
import com.XPilot.XPilot.models.Notificacion;
import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.ContratacionRepository;
import com.XPilot.XPilot.repositories.NotificacionRepository;
import com.XPilot.XPilot.repositories.usuarioRepository;
import com.XPilot.XPilot.services.NotificacionService;

@Controller
public class ContratosController {

    @Autowired private ContratacionRepository contratacionRepo;
    @Autowired private usuarioRepository usuarioRepo;
    @Autowired private NotificacionRepository notifRepo;
    @Autowired private NotificacionService notificacionService;

    private usuario getUser(Principal principal) {
        if (principal == null) throw new RuntimeException("No autenticado");
        return usuarioRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // ── VER CONTRATOS ─────────────────────────────────────────────
    @GetMapping("/mis-contratos")
    public String misContratos(Model model, Principal principal) {
        usuario user = getUser(principal);
        List<Contratacion> comoCliente = contratacionRepo.findByCliente_Id(user.getId());
        List<Contratacion> comoArtista = contratacionRepo.findByArtista_Usuario_Id(user.getId());
        Set<Contratacion> todosSet = new HashSet<>();
        todosSet.addAll(comoCliente);
        todosSet.addAll(comoArtista);
        List<Contratacion> ordenados = new ArrayList<>(todosSet);
        ordenados.sort((a, b) -> {
            if (a.getFechaSolicitud() == null) return 1;
            if (b.getFechaSolicitud() == null) return -1;
            return b.getFechaSolicitud().compareTo(a.getFechaSolicitud());
        });
        model.addAttribute("contratos", ordenados);
        return "mis-contratos";
    }

    // ── HISTORIAL COMPLETO DE NOTIFICACIONES ──────────────────────
    // (endpoint distinto al /api/notificaciones que solo devuelve no leídas)
    @GetMapping("/api/notificaciones/historial")
    @ResponseBody
    public List<Notificacion> historialNotificaciones(Principal principal) {
        if (principal == null) return List.of();
        usuario user = getUser(principal);
        return notifRepo.findByUsuario_IdOrderByIdDesc(user.getId());
    }

    // ── REPROGRAMAR FECHA ─────────────────────────────────────────
    @PostMapping("/contrato/reprogramar/{id}")
    public String reprogramarFecha(@PathVariable Long id,
                                   @RequestParam("fechaEvento") String fechaEvento,
                                   Principal principal) {
        try {
            usuario user = getUser(principal);
            Contratacion c = contratacionRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

            if (c.getCliente() == null || !c.getCliente().getId().equals(user.getId()))
                return "redirect:/mis-contratos";

            if (!"FECHA_RECHAZADA".equals(c.getEstado()))
                return "redirect:/mis-contratos";

            c.setFechaEvento(LocalDate.parse(fechaEvento));
            c.setEstado("PENDIENTE");
            c.setNotificado(false);
            contratacionRepo.save(c);

            // Notificar al admin
            usuario admin = usuarioRepo.findByRol("ADMIN");
            if (admin != null) {
                notificacionService.crearNotificacion(admin,
                    "📅 " + user.getUname() + " reprogramó su contrato con "
                    + c.getArtista().getArtist() + " para el " + fechaEvento);
            }

            // Confirmar al usuario
            notificacionService.crearNotificacion(user,
                "✅ Tu solicitud con " + c.getArtista().getArtist()
                + " fue reprogramada para el "
                + LocalDate.parse(fechaEvento).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + ". Esperando confirmación.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/mis-contratos";
    }

    // ── ACEPTAR CONTRATO ──────────────────────────────────────────
    @PostMapping("/contrato/aceptar/{id}")
    public String aceptarContrato(@PathVariable Long id, Principal principal) {
        try {
            usuario user = getUser(principal);
            Contratacion c = contratacionRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

            if (c.getArtista() != null && c.getArtista().getUsuario() != null &&
                !c.getArtista().getUsuario().getId().equals(user.getId()))
                throw new RuntimeException("No autorizado");

            if (!"ACEPTADO".equals(c.getEstado())) {
                c.setEstado("ACEPTADO");
                contratacionRepo.save(c);
            }

            if (c.getCliente() != null) {
                Notificacion notif = new Notificacion();
                notif.setUsuario(c.getCliente());
                String obraInfo = (c.getObraNombre() != null && !c.getObraNombre().isBlank())
                        ? " — obra \"" + c.getObraNombre() + "\""
                        : "";
                notif.setMensaje("🎉 Tu contrato con " + c.getArtista().getArtist()
                        + obraInfo + " fue aceptado para el " + c.getFechaEvento());
                notif.setLeido(false);
                notifRepo.save(notif);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/mis-contratos";
    }
}
