# 1. Agregar precio a Contratacion
content = open('src/main/java/com/XPilot/XPilot/models/Contratacion.java', encoding='utf-8').read()
content = content.replace(
    '    @Column(length = 200)\n    private String obraNombre;',
    '    @Column(length = 200)\n    private String obraNombre;\n\n    private Double precioObra;'
)
content = content.replace(
    '    public String getObraNombre() { return obraNombre; }\n    public void setObraNombre(String obraNombre) { this.obraNombre = obraNombre; }',
    '    public String getObraNombre() { return obraNombre; }\n    public void setObraNombre(String obraNombre) { this.obraNombre = obraNombre; }\n    public Double getPrecioObra() { return precioObra; }\n    public void setPrecioObra(Double precioObra) { this.precioObra = precioObra; }'
)
open('src/main/java/com/XPilot/XPilot/models/Contratacion.java', 'w', encoding='utf-8').write(content)
print('Contratacion listo')

# 2. GalleryController - guardar precio al contratar
content2 = open('src/main/java/com/XPilot/XPilot/controllers/GalleryController.java', encoding='utf-8').read()
content2 = content2.replace(
    '        if (obraId != null) c.setObraId(obraId);\n        if (obraNombre != null && !obraNombre.isBlank()) c.setObraNombre(obraNombre);\n        if (fechaEvento != null && !fechaEvento.isBlank()) c.setFechaEvento(java.time.LocalDate.parse(fechaEvento));',
    '''        if (obraId != null) c.setObraId(obraId);
        if (obraNombre != null && !obraNombre.isBlank()) c.setObraNombre(obraNombre);
        if (fechaEvento != null && !fechaEvento.isBlank()) c.setFechaEvento(java.time.LocalDate.parse(fechaEvento));
        // Guardar precio de la obra
        if (obraId != null && obraId > 0) {
            mediaFotoRepo.findById(obraId).ifPresent(foto -> c.setPrecioObra(foto.getPrecio()));
        } else {
            c.setPrecioObra(artista.getPrecio());
        }'''
)
open('src/main/java/com/XPilot/XPilot/controllers/GalleryController.java', 'w', encoding='utf-8').write(content2)
print('GalleryController listo')

# 3. AdminController - calcular ganancias por artista
content3 = open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', encoding='utf-8').read()
content3 = content3.replace(
    '        // Contratos por artista\n        java.util.Map<Long, Long> contratacionesPorArtista = new java.util.HashMap<>();\n        for (media m : mediaRepo.findAll()) {\n            long count = contratacionRepo.countByArtista_Id(m.getId());\n            contratacionesPorArtista.put(m.getId(), count);\n        }\n        model.addAttribute("contratacionesPorArtista", contratacionesPorArtista);',
    '''        // Contratos y ganancias por artista
        java.util.Map<Long, Long> contratacionesPorArtista = new java.util.HashMap<>();
        java.util.Map<Long, Double> gananciasPorArtista = new java.util.HashMap<>();
        for (media m : mediaRepo.findAll()) {
            long count = contratacionRepo.countByArtista_Id(m.getId());
            contratacionesPorArtista.put(m.getId(), count);
            double ganancias = contratacionRepo.findByArtista_Id(m.getId()).stream()
                .filter(c -> "ACEPTADO".equals(c.getEstado()) || "FINALIZADO".equals(c.getEstado()))
                .mapToDouble(c -> c.getPrecioObra() != null ? c.getPrecioObra() : 0.0)
                .sum();
            gananciasPorArtista.put(m.getId(), ganancias);
        }
        model.addAttribute("contratacionesPorArtista", contratacionesPorArtista);
        model.addAttribute("gananciasPorArtista", gananciasPorArtista);'''
)
open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', 'w', encoding='utf-8').write(content3)
print('AdminController listo')

# 4. Dashboard - mostrar ganancias
content4 = open('src/main/resources/templates/admin/dashboard.html', encoding='utf-8', errors='ignore').read()
content4 = content4.replace(
    '''                <td>
                  <div th:with="contratos=${contratacionesPorArtista[m.id]}">
                    <div style="font-family:var(--serif);font-size:1.1rem;font-weight:600;" th:text="${contratos != null ? contratos : 0}"></div>
                    <div style="font-size:11px;color:var(--muted2);">contratos</div>
                  </div>
                </td>''',
    '''                <td>
                  <div th:with="contratos=${contratacionesPorArtista[m.id]},ganancias=${gananciasPorArtista[m.id]}">
                    <div style="font-family:var(--serif);font-size:1.1rem;font-weight:600;" th:text="${contratos != null ? contratos : 0}"></div>
                    <div style="font-size:11px;color:var(--muted2);">contratos</div>
                    <div th:if="${ganancias != null and ganancias > 0}" style="font-size:11px;color:var(--amber2);margin-top:2px;" th:text="'$' + ${#numbers.formatInteger(ganancias,1,'COMMA')}"></div>
                  </div>
                </td>'''
)
open('src/main/resources/templates/admin/dashboard.html', 'w', encoding='utf-8').write(content4)
print('Dashboard listo')
