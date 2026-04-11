# Fix dashboard - agregar boton Finalizar y badge FINALIZADO
content = open('src/main/resources/templates/admin/dashboard.html', encoding='utf-8', errors='ignore').read()

content = content.replace(
    '<thead><tr><th>#</th><th>Cliente</th><th>Artista</th><th>Obra</th><th>Fecha</th><th>Estado</th></tr></thead>',
    '<thead><tr><th>#</th><th>Cliente</th><th>Artista</th><th>Obra</th><th>Fecha</th><th>Estado</th><th>Accion</th></tr></thead>'
)

content = content.replace(
    '                  <span th:if="${c.estado == \'PENDIENTE\'}" class="badge bdg-pending">Pendiente</span>\n                  <span th:if="${c.estado == \'ACEPTADO\'}"  class="badge bdg-ok">Aceptado</span>\n                  <span th:if="${c.estado == \'RECHAZADO\'}" class="badge bdg-no">Rechazado</span>\n                </td>\n              </tr>',
    '                  <span th:if="${c.estado == \'PENDIENTE\'}" class="badge bdg-pending">Pendiente</span>\n                  <span th:if="${c.estado == \'ACEPTADO\'}"  class="badge bdg-ok">Aceptado</span>\n                  <span th:if="${c.estado == \'RECHAZADO\'}" class="badge bdg-no">Rechazado</span>\n                  <span th:if="${c.estado == \'FINALIZADO\'}" class="badge" style="background:rgba(100,100,255,0.15);color:#a0a0ff;border:1px solid rgba(100,100,255,0.2);">Finalizado</span>\n                </td>\n                <td>\n                  <form th:if="${c.estado == \'ACEPTADO\'}" th:action="\'/admin/finalizar/\' + ${c.id}" method="post">\n                    <button class="xb xb-sm" style="background:rgba(100,100,255,0.15);color:#a0a0ff;border:1px solid rgba(100,100,255,0.2);">✓ Finalizar</button>\n                  </form>\n                  <span th:unless="${c.estado == \'ACEPTADO\'}" style="color:var(--muted);font-size:12px;">—</span>\n                </td>\n              </tr>'
)

open('src/main/resources/templates/admin/dashboard.html', 'w', encoding='utf-8').write(content)
print('Dashboard listo')

# Fix artista-detalle - agregar precio en obras
content2 = open('src/main/resources/templates/artista-detalle.html', encoding='utf-8', errors='ignore').read()

content2 = content2.replace(
    '        <div class="obra-id">#<span th:text="${foto.id}"></span><span th:if="${foto.nombre != null}" th:text="\' · \' + ${foto.nombre}"></span></div>',
    '        <div class="obra-id">#<span th:text="${foto.id}"></span><span th:if="${foto.nombre != null}" th:text="\' · \' + ${foto.nombre}"></span><span th:if="${foto.precio != null}" th:text="\' — $\' + ${#numbers.formatInteger(foto.precio,1,\'COMMA\')}" style="color:var(--amber2);margin-left:4px;"></span></div>'
)

content2 = content2.replace(
    'function abrirObra(el){',
    '''function abrirObra(el){
  const precio=el.getAttribute('data-precio');
  const precioEl=document.getElementById('obraPrecio');
  if(precioEl) precioEl.textContent=precio&&precio!='null'?'Precio: $'+Number(precio).toLocaleString('es-CO'):'';'''
)

content2 = content2.replace(
    '        <img id="obraImg" src="" style="width:100%;max-height:52vh;object-fit:contain;border-radius:var(--r);margin-bottom:1.5rem;">',
    '        <img id="obraImg" src="" style="width:100%;max-height:52vh;object-fit:contain;border-radius:var(--r);margin-bottom:1rem;">\n        <div id="obraPrecio" style="font-family:var(--serif);font-size:1.2rem;color:var(--amber2);margin-bottom:1rem;"></div>'
)

content2 = content2.replace(
    'th:data-url="${foto.url}"\n           onclick="abrirObra(this)">',
    'th:data-url="${foto.url}"\n           th:data-precio="${foto.precio}"\n           onclick="abrirObra(this)">'
)

open('src/main/resources/templates/artista-detalle.html', 'w', encoding='utf-8').write(content2)
print('Artista-detalle listo')
