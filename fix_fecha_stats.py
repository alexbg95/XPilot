# ── 1. artista-detalle.html: agregar campo fecha en ambos modales ──
content = open('src/main/resources/templates/artista-detalle.html', encoding='utf-8', errors='ignore').read()

# Fecha en modal obra
content = content.replace(
    '          <input type="hidden" name="obraId" id="inObraId">\n          <input type="hidden" name="obraNombre" id="inObraNombre">\n          <button th:if="${artista.disponible}" type="submit"',
    '          <input type="hidden" name="obraId" id="inObraId">\n          <input type="hidden" name="obraNombre" id="inObraNombre">\n          <div class="form-group" style="margin-bottom:1rem;text-align:left;">\n            <label class="label">Fecha del evento</label>\n            <input type="date" name="fechaEvento" class="input" required\n                   style="background:var(--bg3);border:1px solid var(--border);color:var(--white);border-radius:var(--r);padding:10px 14px;font-family:var(--sans);font-size:13px;width:100%;">\n          </div>\n          <button th:if="${artista.disponible}" type="submit"'
)

# Fecha en modal general
content = content.replace(
    '        <form th:action="@{/contratar/{id}(id=${artista.id})}" method="post">\n          <button type="submit" class="xb xb-amber xb-lg xb-full">✓ Confirmar contratación</button>\n        </form>',
    '        <form th:action="@{/contratar/{id}(id=${artista.id})}" method="post">\n          <div class="form-group" style="margin-bottom:1rem;text-align:left;">\n            <label class="label">Fecha del evento</label>\n            <input type="date" name="fechaEvento" class="input" required\n                   style="background:var(--bg3);border:1px solid var(--border);color:var(--white);border-radius:var(--r);padding:10px 14px;font-family:var(--sans);font-size:13px;width:100%;">\n          </div>\n          <button type="submit" class="xb xb-amber xb-lg xb-full">✓ Confirmar contratación</button>\n        </form>'
)

open('src/main/resources/templates/artista-detalle.html', 'w', encoding='utf-8').write(content)
print('artista-detalle listo')

# ── 2. dashboard.html: agregar contratos y ganancias por artista ──
content2 = open('src/main/resources/templates/admin/dashboard.html', encoding='utf-8', errors='ignore').read()

content2 = content2.replace(
    '<thead><tr><th>Artista</th><th>Estilo</th><th>Imagen</th><th>Acciones</th></tr></thead>',
    '<thead><tr><th>Artista</th><th>Estilo</th><th>Imagen</th><th>Contratos</th><th>Disponible</th><th>Acciones</th></tr></thead>'
)

content2 = content2.replace(
    '                <td><div style="display:flex;gap:8px;">\n                  <a th:href="@{/admin/editar/{id}(id=${m.id})}" class="xb xb-outline xb-sm">Editar</a>\n                  <a th:href="@{/admin/eliminar/{id}(id=${m.id})}" class="xb xb-danger xb-sm" onclick="return confirm(\'¿Eliminar artista?\')">Eliminar</a>\n                </div></td>',
    '''                <td>
                  <div th:with="contratos=${contratacionesPorArtista[m.id]}">
                    <div style="font-family:var(--serif);font-size:1.1rem;font-weight:600;" th:text="${contratos != null ? contratos : 0}"></div>
                    <div style="font-size:11px;color:var(--muted2);">contratos</div>
                  </div>
                </td>
                <td>
                  <span th:if="${m.disponible}" style="background:rgba(42,97,69,0.15);color:#4caf80;border:1px solid rgba(76,175,128,0.25);border-radius:20px;padding:3px 10px;font-size:10px;font-weight:700;">Disponible</span>
                  <span th:unless="${m.disponible}" style="background:rgba(122,24,24,0.15);color:#f09090;border:1px solid rgba(240,144,144,0.25);border-radius:20px;padding:3px 10px;font-size:10px;font-weight:700;">Ocupado</span>
                </td>
                <td><div style="display:flex;gap:8px;">
                  <a th:href="@{/admin/editar/{id}(id=${m.id})}" class="xb xb-outline xb-sm">Editar</a>
                  <a th:href="@{/admin/eliminar/{id}(id=${m.id})}" class="xb xb-danger xb-sm" onclick="return confirm(\'¿Eliminar artista?\')">Eliminar</a>
                </div></td>'''
)

open('src/main/resources/templates/admin/dashboard.html', 'w', encoding='utf-8').write(content2)
print('dashboard listo')
