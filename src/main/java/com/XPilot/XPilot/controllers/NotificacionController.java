package com.XPilot.XPilot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

import com.XPilot.XPilot.repositories.NotificacionRepository;
import com.XPilot.XPilot.repositories.usuarioRepository;
import com.XPilot.XPilot.models.Notificacion;
import com.XPilot.XPilot.models.usuario;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionRepository notifRepo;

    @Autowired
    private usuarioRepository usuarioRepo;

    // 🔒 OBTENER USUARIO AUTENTICADO
    private usuario getUser(Principal principal){
        if (principal == null) {
            throw new RuntimeException("No autenticado");
        }

        return usuarioRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // 🔢 CONTADOR
    @GetMapping("/count")
    public long count(Principal principal){
        usuario user = getUser(principal);
        return notifRepo.countByUsuarioIdAndLeidoFalse(user.getId());
    }

    // 📩 LISTAR
    @GetMapping
    public List<Notificacion> listar(Principal principal){
        usuario user = getUser(principal);
        return notifRepo.findByUsuarioIdAndLeidoFalse(user.getId());
    }

    // 🔥 MARCAR COMO LEÍDA
    @PostMapping("/{id}/leer")
    public void marcarComoLeida(@PathVariable Long id, Principal principal){

        usuario user = getUser(principal);

        Notificacion notif = notifRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        // 🔒 VALIDACIÓN DE SEGURIDAD
        if (!notif.getUsuario().getId().equals(user.getId())) {
            throw new RuntimeException("Acceso denegado");
        }

        // 🔥 EVITA UPDATE INNECESARIO
        if (!notif.isLeido()) {
            notif.setLeido(true);
            notifRepo.save(notif);
        }

        System.out.println("🔥 Notificación leída ID: " + id);
    }

    // 📜 HISTORIAL COMPLETO (leídas + no leídas)
    @GetMapping("/historial")
    public List<Notificacion> historial(Principal principal){
        usuario user = getUser(principal);
        return notifRepo.findByUsuario_IdOrderByIdDesc(user.getId());
    }

    // 🔥 MARCAR TODAS
    @PostMapping("/marcar-todas")
    public void marcarTodas(Principal principal){

        usuario user = getUser(principal);

        List<Notificacion> lista = notifRepo.findByUsuarioIdAndLeidoFalse(user.getId());

        if (!lista.isEmpty()) {
            for (Notificacion n : lista) {
                n.setLeido(true);
            }
            notifRepo.saveAll(lista);
        }

        System.out.println("🔥 Todas las notificaciones marcadas como leídas");
    }
}