# Calcular total en controller
content = open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', encoding='utf-8').read()
content = content.replace(
    '        model.addAttribute("gananciasPorArtista", gananciasPorArtista);',
    '        model.addAttribute("gananciasPorArtista", gananciasPorArtista);\n        double totalGanancias = gananciasPorArtista.values().stream().mapToDouble(Double::doubleValue).sum();\n        model.addAttribute("totalGanancias", totalGanancias);'
)
open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', 'w', encoding='utf-8').write(content)
print('Controller listo')

# Fix dashboard - usar totalGanancias
content2 = open('src/main/resources/templates/admin/dashboard.html', encoding='utf-8', errors='ignore').read()
content2 = content2.replace(
    '\'$\' + ${#numbers.formatInteger(gananciasPorArtista.values().stream().mapToDouble(v -> v).sum(),1,\'COMMA\')}',
    '\'$\' + ${#numbers.formatInteger(totalGanancias,1,\'COMMA\')}'
)
open('src/main/resources/templates/admin/dashboard.html', 'w', encoding='utf-8').write(content2)
print('Dashboard listo')
