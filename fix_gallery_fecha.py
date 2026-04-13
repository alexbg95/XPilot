content = open('src/main/java/com/XPilot/XPilot/controllers/GalleryController.java', encoding='utf-8').read()
content = content.replace(
    '@RequestParam(value = "obraNombre", required = false) String obraNombre,',
    '@RequestParam(value = "obraNombre", required = false) String obraNombre,\n            @RequestParam(value = "fechaEvento", required = false) String fechaEvento,'
)
content = content.replace(
    '        if (obraId != null) c.setObraId(obraId);\n        if (obraNombre != null && !obraNombre.isBlank()) c.setObraNombre(obraNombre);',
    '        if (obraId != null) c.setObraId(obraId);\n        if (obraNombre != null && !obraNombre.isBlank()) c.setObraNombre(obraNombre);\n        if (fechaEvento != null && !fechaEvento.isBlank()) c.setFechaEvento(java.time.LocalDate.parse(fechaEvento));'
)
open('src/main/java/com/XPilot/XPilot/controllers/GalleryController.java', 'w', encoding='utf-8').write(content)
print('GalleryController listo')
