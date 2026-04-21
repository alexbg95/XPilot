package com.XPilot.XPilot.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.XPilot.XPilot.models.media;
import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.models.Contratacion;
import com.XPilot.XPilot.models.Notificacion;

import com.XPilot.XPilot.repositories.mediaRepository;
import com.XPilot.XPilot.repositories.usuarioRepository;
import com.XPilot.XPilot.repositories.ContratacionRepository;
import com.XPilot.XPilot.repositories.MediaFotoRepository;
import com.XPilot.XPilot.models.MediaFoto;

import com.XPilot.XPilot.services.NotificationService;
import com.XPilot.XPilot.services.NotificacionService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private mediaRepository mediaRepo;
    @Autowired private usuarioRepository usuarioRepo;
    @Autowired private ContratacionRepository contratacionRepo;
    @Autowired private NotificationService notificationService;
    @Autowired private NotificacionService notificacionService;
    @Autowired private Cloudinary cloudinary;
    @Autowired private MediaFotoRepository mediaFotoRepo;

    // ================= DASHBOARD =================
    @GetMapping
    public String adminDashboard(Model model){
        model.addAttribute("mediaList",   mediaRepo.findAll());
        model.addAttribute("solicitudes", contratacionRepo.findByEstado("PENDIENTE"));
        model.addAttribute("todos",       contratacionRepo.findAll());
        model.addAttribute("aceptados",   contratacionRepo.findByEstado("ACEPTADO"));
        model.addAttribute("rechazados",  contratacionRepo.findByEstado("RECHAZADO"));

        java.util.Map<String, Long> contratacionesPorArtista = new java.util.HashMap<>();
        java.util.Map<String, Double> gananciasPorArtista = new java.util.HashMap<>();
        for (media m : mediaRepo.findAll()) {
            long count = contratacionRepo.countByArtista_Id(m.getId());
            contratacionesPorArtista.put(String.valueOf(m.getId()), count);
            System.out.println("🔍 Calculando ganancias artista: " + m.getArtist());
            double ganancias = contratacionRepo.findByArtista_Id(m.getId()).stream()
                .filter(c -> "ACEPTADO".equals(c.getEstado()) || "FINALIZADO".equals(c.getEstado()))
                .mapToDouble(c -> {
                    if (c.getPrecioObra() != null) { System.out.println("  precioObra: " + c.getPrecioObra()); return c.getPrecioObra(); }
                    if (c.getObraId() != null && c.getObraId() > 0) {
                        double p = mediaFotoRepo.findById(c.getObraId()).map(f -> f.getPrecio() != null ? f.getPrecio() : 0.0).orElse(0.0); System.out.println("  obraId: " + c.getObraId() + " precio: " + p); return p;
                    }
                    return c.getArtista() != null && c.getArtista().getPrecio() != null ? c.getArtista().getPrecio() : 0.0;
                })
                .sum();
            gananciasPorArtista.put(String.valueOf(m.getId()), ganancias);
        }
        model.addAttribute("contratacionesPorArtista", contratacionesPorArtista);
        model.addAttribute("gananciasPorArtista", gananciasPorArtista);
        double totalGanancias = gananciasPorArtista.values().stream().mapToDouble(Double::doubleValue).sum();
        model.addAttribute("totalGanancias", totalGanancias);

        return "admin/dashboard";
    }

    // ================= CREAR ARTISTA =================
    @GetMapping("/nuevo")
    public String nuevoArtista(Model model){
        model.addAttribute("media", new media());
        return "admin/crear-artista";
    }

    // ================= GUARDAR CON CLOUDINARY =================
    @PostMapping("/guardar")
    public String guardarArtista(
            @ModelAttribute media media,
            @RequestParam(value = "fotoPerfil",   required = false) MultipartFile fotoPerfil,
            @RequestParam(value = "obraArte",     required = false) MultipartFile obraArte,
            @RequestParam(value = "urlmActual",   required = false) String urlmActual,
            @RequestParam(value = "tagsActual",   required = false) String tagsActual,
            @RequestParam(value = "obrasExtra",   required = false) List<MultipartFile> obrasExtra,
            @RequestParam(value = "nombresObras", required = false) List<String> nombresObras,
            @RequestParam(value = "preciosObras", required = false) List<Double> preciosObras,
            @RequestParam(value = "precio",       required = false) Double precio,
            Principal principal) {

        if (principal == null) return "redirect:/login-view";

        usuario user = usuarioRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        media.setUsuario(user);

        if (urlmActual != null && !urlmActual.isBlank()) media.setUrlm(urlmActual);
        if (tagsActual != null && !tagsActual.isBlank()) media.setTags(tagsActual);

        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> resultado = cloudinary.uploader().upload(
                        fotoPerfil.getBytes(), ObjectUtils.asMap("folder", "xpilot/perfiles"));
                media.setUrlm((String) resultado.get("secure_url"));
            } catch (Exception e) {
                System.out.println("❌ Error subiendo foto perfil: " + e.getMessage());
            }
        }

        if (obraArte != null && !obraArte.isEmpty()) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> resultado = cloudinary.uploader().upload(
                        obraArte.getBytes(), ObjectUtils.asMap("folder", "xpilot/obras"));
                media.setTags((String) resultado.get("secure_url"));
            } catch (Exception e) {
                System.out.println("❌ Error subiendo obra: " + e.getMessage());
            }
        }

        if (precio != null) media.setPrecio(precio);
        media saved = mediaRepo.save(media);

        if (obrasExtra != null) {
            for (int i = 0; i < obrasExtra.size(); i++) {
                MultipartFile foto = obrasExtra.get(i);
                if (foto != null && !foto.isEmpty()) {
                    try {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> res = cloudinary.uploader().upload(
                                foto.getBytes(), ObjectUtils.asMap("folder", "xpilot/obras-extra"));
                        String url = (String) res.get("secure_url");
                        String nombreObra = (nombresObras != null && i < nombresObras.size()
                                && !nombresObras.get(i).isBlank())
                                ? nombresObras.get(i) : "Obra " + (i + 1);
                        Double precioObra = (preciosObras != null && i < preciosObras.size()) ? preciosObras.get(i) : null;
                        MediaFoto mf = new MediaFoto(url, nombreObra, saved);
                        mf.setPrecio(precioObra);
                        mediaFotoRepo.save(mf);
                    } catch (Exception e) {
                        System.out.println("❌ Error subiendo foto extra: " + e.getMessage());
                    }
                }
            }
        }

        return "redirect:/admin";
    }

    // ================= EDITAR =================
    @GetMapping("/editar/{id}")
    public String editarArtista(@PathVariable Long id, Model model){
        media media = mediaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista no encontrado"));
        model.addAttribute("media", media);
        model.addAttribute("fotosExistentes", mediaFotoRepo.findByMedia(media));
        return "admin/crear-artista";
    }

    // ================= PRECIO OBRA =================
    @PostMapping("/precio-obra/{fotoId}")
    public String actualizarPrecioObra(@PathVariable Long fotoId,
                                       @RequestParam Double precio,
                                       @RequestParam(required=false) Long artistaId) {
        System.out.println("✅ Actualizando precio foto ID: " + fotoId + " precio: " + precio);
        mediaFotoRepo.findById(fotoId).ifPresent(foto -> {
            foto.setPrecio(precio);
            mediaFotoRepo.save(foto);
        });
        Long aid = mediaFotoRepo.findById(fotoId).map(f -> f.getMedia().getId()).orElse(artistaId);
        return "redirect:/admin/editar/" + aid;
    }

    // ================= ELIMINAR FOTO OBRA =================
    @Transactional
    @GetMapping("/eliminar-foto/{fotoId}")
    public String eliminarFotoObra(@PathVariable Long fotoId, @RequestParam Long artistaId) {
        mediaFotoRepo.deleteById(fotoId);
        return "redirect:/admin/editar/" + artistaId;
    }

    // ================= ELIMINAR =================
    @Transactional
    @GetMapping("/eliminar/{id}")
    public String eliminarArtista(@PathVariable Long id){
        contratacionRepo.deleteByArtista_Id(id);
        if (mediaRepo.existsById(id)) { mediaRepo.deleteById(id); }
        return "redirect:/admin";
    }

    // ================= ACEPTAR + NOTIFICAR =================
    @PostMapping("/aceptar/{id}")
    public String aceptarSolicitud(@PathVariable Long id,
                                   @RequestParam("fechaEvento") String fechaEvento){

        Contratacion c = contratacionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        c.setEstado("ACEPTADO");
        c.setFechaEvento(LocalDate.parse(fechaEvento));
        if (c.getArtista() != null) {
            c.getArtista().setDisponible(false);
            mediaRepo.save(c.getArtista());
        }

        try {
            if (c.getArtista() != null && c.getArtista().getUsuario() != null) {
                usuario artistaUser = c.getArtista().getUsuario();
                String msg = "🎤 Has sido contratado para el " + c.getFechaEvento();
                notificacionService.crearNotificacion(artistaUser, msg);
                if (artistaUser.getFcmToken() != null && !artistaUser.getFcmToken().isBlank())
                    notificationService.enviarNotificacion(artistaUser.getFcmToken(), "Nuevo contrato", msg);
            }
            if (c.getCliente() != null) {
                usuario cliente = c.getCliente();
                String msg = "🎉 Tu contrato con " + c.getArtista().getArtist()
                           + " fue aceptado para el " + c.getFechaEvento();
                notificacionService.crearNotificacion(cliente, msg);
                if (cliente.getFcmToken() != null && !cliente.getFcmToken().isBlank())
                    notificationService.enviarNotificacion(cliente.getFcmToken(), "Contrato aceptado", msg);
            }
            if (!c.isNotificado()) c.setNotificado(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        contratacionRepo.save(c);
        return "redirect:/admin";
    }

    // ================= RECHAZAR + NOTIFICAR =================
    @PostMapping("/rechazar/{id}")
    public String rechazarSolicitud(@PathVariable Long id){

        Contratacion c = contratacionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        c.setEstado("RECHAZADO");
        c.setNotificado(true);
        contratacionRepo.save(c);

        try {
            if (c.getCliente() != null) {
                usuario cliente = c.getCliente();
                String titulo = "❌ Contrato rechazado";
                String msg = "Tu solicitud con " + c.getArtista().getArtist() + " fue rechazada por el administrador.";
                notificacionService.crearNotificacion(cliente, titulo + " — " + msg);
                if (cliente.getFcmToken() != null && !cliente.getFcmToken().isBlank())
                    notificationService.enviarNotificacion(cliente.getFcmToken(), titulo, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin";
    }

    // ================= 📅 NO DISPONIBLE PARA FECHA =================
    @PostMapping("/no-disponible/{id}")
    public String noDisponibleFecha(@PathVariable Long id,
                                    @RequestParam("mensaje") String mensaje){

        Contratacion c = contratacionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        // Estado especial: usuario puede reprogramar desde Mis Contratos
        c.setEstado("FECHA_RECHAZADA");
        c.setNotificado(true);
        contratacionRepo.save(c);

        // Artista vuelve a estar disponible
        if (c.getArtista() != null) {
            c.getArtista().setDisponible(true);
            mediaRepo.save(c.getArtista());
        }

        try {
            if (c.getCliente() != null) {
                usuario cliente = c.getCliente();
                String titulo = "📅 Artista no disponible para esa fecha";
                String msg = "⚠️ El artista " + c.getArtista().getArtist()
                           + " no está disponible para la fecha solicitada. Motivo: " + mensaje
                           + ". Puedes reprogramar la fecha desde Mis Contratos.";
                notificacionService.crearNotificacion(cliente, msg);
                if (cliente.getFcmToken() != null && !cliente.getFcmToken().isBlank())
                    notificationService.enviarNotificacion(cliente.getFcmToken(), titulo, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin";
    }

    // ================= FINALIZAR =================
    @PostMapping("/finalizar/{id}")
    public String finalizarContrato(@PathVariable Long id){
        Contratacion c = contratacionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
        c.setEstado("FINALIZADO");
        contratacionRepo.save(c);
        if (c.getArtista() != null) {
            c.getArtista().setDisponible(true);
            mediaRepo.save(c.getArtista());
        }
        try {
            if (c.getArtista() != null && c.getArtista().getUsuario() != null)
                notificacionService.crearNotificacion(c.getArtista().getUsuario(),
                    "Tu contrato del " + c.getFechaEvento() + " ha sido marcado como finalizado.");
            if (c.getCliente() != null)
                notificacionService.crearNotificacion(c.getCliente(),
                    "Tu contrato con " + c.getArtista().getArtist() + " ha finalizado. Gracias por usar XPilot.");
        } catch (Exception e) { e.printStackTrace(); }
        return "redirect:/admin";
    }

    // ================= NOTIFICACIONES ADMIN =================
    @GetMapping("/notificaciones")
    @ResponseBody
    public List<Notificacion> obtenerNotificaciones(Principal principal){
        if (principal == null) throw new RuntimeException("No autenticado");
        usuario user = usuarioRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return notificacionService.obtenerNoLeidas(user.getId());
    }

    // ================= GUARDAR TOKEN =================
    @PostMapping("/guardar-token")
    @ResponseBody
    public String guardarToken(@RequestBody Map<String, String> body, Principal principal){
        try {
            if (principal == null) return "NO AUTH";
            String token = body.get("token");
            if (token == null || token.isBlank()) return "TOKEN VACIO";
            usuario user = usuarioRepo.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            user.setFcmToken(token);
            usuarioRepo.save(user);
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
