package com.XPilot.XPilot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.XPilot.XPilot.models.Notificacion;
import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.NotificacionRepository;

import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notifRepo;

    // 🔔 CREAR NOTIFICACIÓN
    public void crearNotificacion(usuario user, String mensaje){

        if (user == null) {
            System.out.println("❌ Usuario es NULL");
            return;
        }

        try {
            Notificacion n = new Notificacion();

            n.setUsuario(user);
            n.setMensaje(mensaje);
            n.setLeido(false); // 🔥 CORREGIDO

            notifRepo.save(n);

            System.out.println("✅ Notificación guardada para usuario ID: " + user.getId());

        } catch (Exception e) {
            System.out.println("❌ Error guardando notificación: " + e.getMessage());
        }
    }

    // 📩 OBTENER NO LEÍDAS
    public List<Notificacion> obtenerNoLeidas(Long usuarioId){
        return notifRepo.findByUsuarioIdAndLeidoFalse(usuarioId); // 🔥 CORREGIDO
    }

    // 🔢 CONTADOR
    public long contarNoLeidas(Long usuarioId){
        return notifRepo.countByUsuarioIdAndLeidoFalse(usuarioId);
    }

    // 🔥 MARCAR UNA
    public void marcarLeida(Long id){

        Notificacion notif = notifRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notif.setLeido(true);
        notifRepo.save(notif);

        System.out.println("🔥 Notificación marcada como leída ID: " + id);
    }

    // 🔥 MARCAR TODAS
    public void marcarTodas(Long usuarioId){

        List<Notificacion> lista = notifRepo.findByUsuarioIdAndLeidoFalse(usuarioId);

        for (Notificacion n : lista) {
            n.setLeido(true);
        }

        notifRepo.saveAll(lista);

        System.out.println("🔥 Todas las notificaciones marcadas como leídas");
    }
}