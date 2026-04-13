content = open('src/main/resources/templates/admin/crear-artista.html', encoding='utf-8', errors='ignore').read()

# Agregar campo precio en cada obra extra del preview
content = content.replace(
    '''      const w = document.createElement("div");
      w.style = "display:flex;align-items:center;gap:14px;padding:12px;background:var(--bg3);border:1px solid var(--border);border-radius:var(--r);";
      const n = file.name.split('.').slice(0, -1).join('.');
      w.innerHTML = `<img src="${e.target.result}" style="width:70px;height:70px;object-fit:cover;border-radius:var(--r);flex-shrink:0;">
        <div style="flex:1;">
          <label class="label">Nombre obra ${i + 1}</label>
          <input type="text" name="nombresObras" class="input" placeholder="Ej: Atardecer oscuro" value="${n}">
        </div>`;''',
    '''      const w = document.createElement("div");
      w.style = "display:flex;align-items:center;gap:14px;padding:12px;background:var(--bg3);border:1px solid var(--border);border-radius:var(--r);";
      const n = file.name.split('.').slice(0, -1).join('.');
      w.innerHTML = `<img src="${e.target.result}" style="width:70px;height:70px;object-fit:cover;border-radius:var(--r);flex-shrink:0;">
        <div style="flex:1;display:flex;gap:10px;">
          <div style="flex:2;">
            <label class="label">Nombre obra ${i + 1}</label>
            <input type="text" name="nombresObras" class="input" placeholder="Ej: Atardecer oscuro" value="${n}">
          </div>
          <div style="flex:1;">
            <label class="label">Precio (COP)</label>
            <input type="number" name="preciosObras" class="input" placeholder="ej: 500000" min="0" step="1000">
          </div>
        </div>`;'''
)

open('src/main/resources/templates/admin/crear-artista.html', 'w', encoding='utf-8').write(content)
print('Formulario listo')
