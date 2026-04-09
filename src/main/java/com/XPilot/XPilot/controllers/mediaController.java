package com.XPilot.XPilot.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.XPilot.XPilot.models.media;
import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.services.mediaServicesManager;
import com.XPilot.XPilot.repositories.usuarioRepository;

@RestController
@RequestMapping("/api/media")
public class mediaController {

    @Autowired
    private mediaServicesManager servicesManager;

    @Autowired
    private usuarioRepository usuarioRepository;

    // ✅ Obtener todos los media
    @GetMapping
    @Transactional(readOnly = true)
    public List<media> getAllmedia() {
        return servicesManager.all();
    }

    // ✅ Buscar media por ID
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public media findMediaByID(@PathVariable long id) {
        return servicesManager.find(id);
    }

    // ✅ Crear un media sin usuario
    @PostMapping
    public media saveMedia(@RequestBody media media) {
        return servicesManager.save(media);
    }

    // ✅ Crear un media asociado a un usuario
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> saveMediaForUser(@PathVariable long usuarioId, @RequestBody media media) {
        Optional<usuario> userOpt = usuarioRepository.findById(usuarioId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Usuario no encontrado con ID: " + usuarioId);
        }
        usuario user = userOpt.get();
        media.setUsuario(user);
        media savedMedia = servicesManager.save(media);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMedia);
    }

    // ✅ Actualizar media
    @PutMapping("/{id}")
    public media updateMedia(@PathVariable long id, @RequestBody media media) {
        return servicesManager.update(id, media);
    }

    // ✅ Eliminar media
    @DeleteMapping("/{id}")
    public void deleteMedia(@PathVariable long id) {
        servicesManager.delete(id);
    }

    // ✅ Obtener todos los media de un usuario
    @GetMapping("/usuario/{usuarioId}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getMediaByUser(@PathVariable long usuarioId) {
        Optional<usuario> userOpt = usuarioRepository.findById(usuarioId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Usuario no encontrado con ID: " + usuarioId);
        }
        List<media> mediaList = userOpt.get().getMedia();
        return ResponseEntity.ok(mediaList);
    }

    // ☁️ Subir imagen a Cloudinary y guardar media
    @PostMapping("/upload")
    public ResponseEntity<?> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("artist") String artist,
            @RequestParam("style") String style,
            @RequestParam("tags") String tags,
            @RequestParam(value = "usuarioId", required = false) Long usuarioId) {
        try {
            String url = servicesManager.uploadToCloudinary(file);

            media m = new media();
            m.setArtist(artist);
            m.setStyle(style);
            m.setTags(tags);
            m.setUrlm(url);
            m.setDatem(java.time.LocalDate.now());

            if (usuarioId != null) {
                usuarioRepository.findById(usuarioId).ifPresent(m::setUsuario);
            }

            media saved = servicesManager.save(m);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error al subir imagen: " + e.getMessage());
        }
    }
}
