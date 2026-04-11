content = open('src/main/resources/templates/admin/crear-artista.html', encoding='utf-8', errors='ignore').read()
# Agregar campo precio despues de biografia
content = content.replace(
    '</textarea>',
    '</textarea>\n      </div>\n      <div class="form-group" style="margin-top:1rem;">\n        <label class="label">Precio base (COP)</label>\n        <input type="number" name="precio" class="input" placeholder="ej: 500000" th:value="${media.precio}" min="0" step="1000">\n      </div>\n      <div style="display:none">',
    1
)
open('src/main/resources/templates/admin/crear-artista.html', 'w', encoding='utf-8').write(content)
print('Listo')
