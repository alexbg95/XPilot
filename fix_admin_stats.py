content = open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', encoding='utf-8').read()

content = content.replace(
    '    @GetMapping\n    public String adminDashboard(Model model){\n        model.addAttribute("mediaList",   mediaRepo.findAll());\n        model.addAttribute("solicitudes", contratacionRepo.findByEstado("PENDIENTE"));\n        model.addAttribute("todos",       contratacionRepo.findAll());\n        model.addAttribute("aceptados",   contratacionRepo.findByEstado("ACEPTADO"));\n        model.addAttribute("rechazados",  contratacionRepo.findByEstado("RECHAZADO"));\n        return "admin/dashboard";\n    }',
    '''    @GetMapping
    public String adminDashboard(Model model){
        model.addAttribute("mediaList",   mediaRepo.findAll());
        model.addAttribute("solicitudes", contratacionRepo.findByEstado("PENDIENTE"));
        model.addAttribute("todos",       contratacionRepo.findAll());
        model.addAttribute("aceptados",   contratacionRepo.findByEstado("ACEPTADO"));
        model.addAttribute("rechazados",  contratacionRepo.findByEstado("RECHAZADO"));

        // Contratos por artista
        java.util.Map<Long, Long> contratacionesPorArtista = new java.util.HashMap<>();
        for (media m : mediaRepo.findAll()) {
            long count = contratacionRepo.countByArtista_Id(m.getId());
            contratacionesPorArtista.put(m.getId(), count);
        }
        model.addAttribute("contratacionesPorArtista", contratacionesPorArtista);

        return "admin/dashboard";
    }'''
)

open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', 'w', encoding='utf-8').write(content)
print('AdminController listo')
