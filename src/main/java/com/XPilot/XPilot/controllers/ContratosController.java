package com.XPilot.XPilot.controllers;

import java.security.Principal;
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

@Controller
public class ContratosController {

    @Autowired
    private ContratacionRepository contratacionRepo;

    @Autowired
    private usuarioRepository usuarioRepo;

    @Autowired
    private NotificacionRepository notifRepo;

    // 🔒 USUARIO AUTENTICADO
    private usuario getUser(Principal principal){
        if (principal == null) {
            throw new RuntimeException("No autenticado");
        }

        return usuarioRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // 📄 VER CONTRATOS
    @GetMapping("/mis-contratos")
    public String misContratos(Model model, Principal principal) {

        usuario user = getUser(principal);

        List<Contratacion> comoCliente =
                contratacionRepo.findByCliente_Id(user.getId());

        List<Contratacion> comoArtista =
                contratacionRepo.findByArtista_Usuario_Id(user.getId());

        Set<Contratacion> todosSet = new HashSet<>();
        todosSet.addAll(comoCliente);
        todosSet.addAll(comoArtista);

        model.addAttribute("contratos", new ArrayList<>(todosSet));

        return "mis-contratos";
    }

    // 🔥 ACEPTAR CONTRATO
    @PostMapping("/contrato/aceptar/{id}")
    public String aceptarContrato(@PathVariable Long id, Principal principal) {

        try {

            usuario user = getUser(principal);

            Contratacion c = contratacionRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

            // 🔒 VALIDACIÓN
            if (c.getArtista() != null &&
                c.getArtista().getUsuario() != null &&
                !c.getArtista().getUsuario().getId().equals(user.getId())) {

                throw new RuntimeException("No autorizado");
            }

            // 🔥 ACTUALIZA ESTADO
            if (!"ACEPTADO".equals(c.getEstado())) {
                c.setEstado("ACEPTADO");
                contratacionRepo.save(c);
            }

            // 🔔 NOTIFICACIÓN
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

                System.out.println("🔥 Notificación creada");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/mis-contratos";
    }
}