content = open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', encoding='utf-8').read()
content = content.replace(
    '    // ================= NOTIFICACIONES ADMIN =================',
    '''    // ================= FINALIZAR =================
    @PostMapping("/finalizar/{id}")
    public String finalizarContrato(@PathVariable Long id){
        Contratacion c = contratacionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
        c.setEstado("FINALIZADO");
        contratacionRepo.save(c);
        // Artista queda disponible
        if (c.getArtista() != null) {
            media artista = c.getArtista();
            artista.setDisponible(true);
            mediaRepo.save(artista);
        }
        try {
            if (c.getArtista() != null && c.getArtista().getUsuario() != null) {
                usuario artistaUser = c.getArtista().getUsuario();
                String msg = "Tu contrato del " + c.getFechaEvento() + " ha sido marcado como finalizado.";
                notificacionService.crearNotificacion(artistaUser, msg);
            }
            if (c.getCliente() != null) {
                String msg = "Tu contrato con " + c.getArtista().getArtist() + " ha finalizado. Gracias por usar XPilot.";
                notificacionService.crearNotificacion(c.getCliente(), msg);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "redirect:/admin";
    }

    // ================= NOTIFICACIONES ADMIN ================='''
)
open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', 'w', encoding='utf-8').write(content)
print('Listo')
