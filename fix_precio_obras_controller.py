content = open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', encoding='utf-8').read()

content = content.replace(
    '@RequestParam(value = "nombresObras", required = false) List<String> nombresObras,',
    '@RequestParam(value = "nombresObras", required = false) List<String> nombresObras,\n            @RequestParam(value = "preciosObras", required = false) List<Double> preciosObras,'
)

content = content.replace(
    '                        String nombreObra = (nombresObras != null && i < nombresObras.size()\n                                && !nombresObras.get(i).isBlank())\n                                ? nombresObras.get(i) : "Obra " + (i + 1);\n                        mediaFotoRepo.save(new MediaFoto(url, nombreObra, saved));',
    '                        String nombreObra = (nombresObras != null && i < nombresObras.size()\n                                && !nombresObras.get(i).isBlank())\n                                ? nombresObras.get(i) : "Obra " + (i + 1);\n                        Double precioObra = (preciosObras != null && i < preciosObras.size()) ? preciosObras.get(i) : null;\n                        MediaFoto mf = new MediaFoto(url, nombreObra, saved);\n                        mf.setPrecio(precioObra);\n                        mediaFotoRepo.save(mf);'
)

open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', 'w', encoding='utf-8').write(content)
print('Controller listo')
