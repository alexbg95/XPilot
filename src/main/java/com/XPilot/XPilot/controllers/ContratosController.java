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

    // ── USUARIO AUTENTICADO ───────────────────────────────────────
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

        model.addAttribute("contratos", new ArrayList<>(todosSet));
        return "mis-contratos";
    }

    // ── HISTORIAL DE NOTIFICACIONES (todas, no solo no leídas) ────
    @GetMapping("/api/notificaciones/historial")
    @ResponseBody
    public List<Notificacion> historialNotificaciones(Principal principal) {
        if (principal == null) return List.of();
        usuario user = getUser(principal);
        // Devuelve todas las notificaciones del usuario ordenadas por id desc
        return notifRepo.findByUsuario_IdOrderByIdDesc(user.getId());
    }

    // ── NOTIFICACIONES NO LEÍDAS (para el badge) ──────────────────
    @GetMapping("/api/notificaciones")
    @ResponseBody
    public List<Notificacion> notificacionesNoLeidas(Principal principal) {
        if (principal == null) return List.of();
        usuario user = getUser(principal);
        return notificacionService.obtenerNoLeidas(user.getId());
    }

    // ── MARCAR NOTIFICACIÓN COMO LEÍDA ────────────────────────────
    @PostMapping("/api/notificaciones/{id}/leer")
    @ResponseBody
    public String marcarLeida(@PathVariable Long id, Principal principal) {
        try {
            notifRepo.findById(id).ifPresent(n -> {
                n.setLeido(true);
                notifRepo.save(n);
            });
            return "OK";
        } catch (Exception e) {
            return "ERROR";
        }
    }

    // ── MARCAR TODAS COMO LEÍDAS ──────────────────────────────────
    @PostMapping("/api/notificaciones/marcar-todas")
    @ResponseBody
    public String marcarTodas(Principal principal) {
        try {
            usuario user = getUser(principal);
            List<Notificacion> lista = notificacionService.obtenerNoLeidas(user.getId());
            lista.forEach(n -> { n.setLeido(true); notifRepo.save(n); });
            return "OK";
        } catch (Exception e) {
            return "ERROR";
        }
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

            // Validar que el contrato pertenece al usuario
            if (c.getCliente() == null || !c.getCliente().getId().equals(user.getId())) {
                return "redirect:/mis-contratos";
            }

            // Solo se puede reprogramar si la fecha fue rechazada
            if (!"FECHA_RECHAZADA".equals(c.getEstado())) {
                return "redirect:/mis-contratos";
            }

            // Actualizar fecha y volver a PENDIENTE
            c.setFechaEvento(LocalDate.parse(fechaEvento));
            c.setEstado("PENDIENTE");
            c.setNotificado(false);
            contratacionRepo.save(c);

            // Notificar al admin
            usuario admin = usuarioRepo.findByRol("ADMIN");
            if (admin != null) {
                String msg = "📅 " + user.getUname() + " reprogramó su contrato con "
                           + c.getArtista().getArtist() + " para el " + fechaEvento;
                notificacionService.crearNotificacion(admin, msg);
            }

            // Notificar al usuario confirmando
            String msgUsuario = "✅ Tu solicitud con " + c.getArtista().getArtist()
                              + " fue reprogramada para el " + LocalDate.parse(fechaEvento)
                                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                              + ". Esperando confirmación del admin.";
            notificacionService.crearNotificacion(user, msgUsuario);

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

            if (c.getArtista() != null &&
                c.getArtista().getUsuario() != null &&
                !c.getArtista().getUsuario().getId().equals(user.getId())) {
                throw new RuntimeException("No autorizado");
            }

            if (!"ACEPTADO".equals(c.getEstado())) {
                c.setEstado("ACEPTADO");
                contratacionRepo.save(c);
            }

            usuario cliente = c.getCliente();
            if (cliente != null) {
                Notificacion notif = new Notificacion();
                notif.setUsuario(cliente);
                notif.setMensaje("🎉 Tu contrato con "
                        + c.getArtista().getArtist()
                        + " fue aceptado para el "
                        + c.getFechaEvento());
                notif.setLeido(false);
                notifRepo.save(notif);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/mis-contratos";
    }
}
