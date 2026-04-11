content = open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', encoding='utf-8').read()
content = content.replace(
    '@RequestParam(value = "nombresObras", required = false) List<String> nombresObras,\n            Principal principal)',
    '@RequestParam(value = "nombresObras", required = false) List<String> nombresObras,\n            @RequestParam(value = "precio", required = false) Double precio,\n            Principal principal)'
)
content = content.replace(
    'media saved = mediaRepo.save(media);',
    'if (precio != null) media.setPrecio(precio);\n        media saved = mediaRepo.save(media);'
)
open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', 'w', encoding='utf-8').write(content)
print('Listo')
