# 1. GalleryController - bloquear contratacion si no disponible
content = open('src/main/java/com/XPilot/XPilot/controllers/GalleryController.java', encoding='utf-8').read()
content = content.replace(
    '        Contratacion c = new Contratacion();',
    '        // Verificar disponibilidad\n        if (!artista.isDisponible()) {\n            return "redirect:/artista/" + artistaId + "?ocupado=true";\n        }\n\n        // Marcar artista como no disponible\n        artista.setDisponible(false);\n        mediaRepo.save(artista);\n\n        Contratacion c = new Contratacion();'
)
open('src/main/java/com/XPilot/XPilot/controllers/GalleryController.java', 'w', encoding='utf-8').write(content)
print('GalleryController listo')

# 2. AdminController - marcar no disponible al aceptar
content2 = open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', encoding='utf-8').read()
content2 = content2.replace(
    '        c.setEstado("ACEPTADO");\n        c.setFechaEvento(LocalDate.parse(fechaEvento));',
    '        c.setEstado("ACEPTADO");\n        c.setFechaEvento(LocalDate.parse(fechaEvento));\n        // Marcar artista no disponible\n        if (c.getArtista() != null) {\n            c.getArtista().setDisponible(false);\n            mediaRepo.save(c.getArtista());\n        }'
)
open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', 'w', encoding='utf-8').write(content2)
print('AdminController listo')

# 3. gallery.html - agregar badge disponible/ocupado
content3 = open('src/main/resources/templates/gallery.html', encoding='utf-8', errors='ignore').read()
content3 = content3.replace(
    '        <div class="card-actions">',
    '        <div style="position:absolute;top:12px;right:12px;z-index:2;">\n          <span th:if="${m.disponible}" style="background:rgba(42,97,69,0.9);color:#4caf80;border:1px solid rgba(76,175,128,0.3);border-radius:20px;padding:3px 10px;font-size:10px;font-weight:700;letter-spacing:0.08em;text-transform:uppercase;">Disponible</span>\n          <span th:unless="${m.disponible}" style="background:rgba(122,24,24,0.9);color:#f09090;border:1px solid rgba(240,144,144,0.3);border-radius:20px;padding:3px 10px;font-size:10px;font-weight:700;letter-spacing:0.08em;text-transform:uppercase;">Ocupado</span>\n        </div>\n        <div class="card-actions">'
)
open('src/main/resources/templates/gallery.html', 'w', encoding='utf-8').write(content3)
print('gallery.html listo')

# 4. artista-detalle.html - mostrar disponibilidad y bloquear boton
content4 = open('src/main/resources/templates/artista-detalle.html', encoding='utf-8', errors='ignore').read()
# Agregar badge disponibilidad junto al boton contratar
content4 = content4.replace(
    '        <button class="xb xb-amber xb-lg" onclick="abrirModalContratar()">🎧 Contratar Artista</button>',
    '''        <div style="display:flex;align-items:center;gap:1rem;flex-wrap:wrap;">
          <span th:if="${artista.disponible}" style="background:rgba(42,97,69,0.15);color:#4caf80;border:1px solid rgba(76,175,128,0.25);border-radius:20px;padding:5px 14px;font-size:11px;font-weight:700;letter-spacing:0.08em;text-transform:uppercase;">● Disponible</span>
          <span th:unless="${artista.disponible}" style="background:rgba(122,24,24,0.15);color:#f09090;border:1px solid rgba(240,144,144,0.25);border-radius:20px;padding:5px 14px;font-size:11px;font-weight:700;letter-spacing:0.08em;text-transform:uppercase;">● Ocupado</span>
          <button th:if="${artista.disponible}" class="xb xb-amber xb-lg" onclick="abrirModalContratar()">🎧 Contratar Artista</button>
          <button th:unless="${artista.disponible}" class="xb xb-lg" disabled style="opacity:0.4;cursor:not-allowed;">No disponible</button>
        </div>
        <div th:if="${param.ocupado}" style="margin-top:1rem;padding:12px 16px;background:rgba(122,24,24,0.1);border:1px solid rgba(240,144,144,0.2);border-radius:8px;color:#f09090;font-size:13px;">
          ⚠️ Este artista ya tiene un contrato activo y no está disponible en este momento.
        </div>'''
)

# Bloquear boton en modal obra si no disponible
content4 = content4.replace(
    '          <button type="submit" class="xb xb-amber xb-lg xb-full">\n            🎧 Contratar a <span th:text="${artista.artist}"></span> por esta obra\n          </button>',
    '          <button th:if="${artista.disponible}" type="submit" class="xb xb-amber xb-lg xb-full">🎧 Contratar a <span th:text="${artista.artist}"></span> por esta obra</button>\n          <button th:unless="${artista.disponible}" type="button" disabled class="xb xb-lg xb-full" style="opacity:0.4;cursor:not-allowed;">Artista no disponible</button>'
)

open('src/main/resources/templates/artista-detalle.html', 'w', encoding='utf-8').write(content4)
print('artista-detalle listo')
