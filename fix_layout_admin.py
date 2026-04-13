# 1. Dashboard - ganancias al lado de contratos y agregar fecha en historial
content = open('src/main/resources/templates/admin/dashboard.html', encoding='utf-8', errors='ignore').read()

# Ganancias al lado de contratos
content = content.replace(
    '''                <td>
                  <div th:with="contratos=${contratacionesPorArtista[m.id]},ganancias=${gananciasPorArtista[m.id]}">
                    <div style="font-family:var(--serif);font-size:1.1rem;font-weight:600;" th:text="${contratos != null ? contratos : 0}"></div>
                    <div style="font-size:11px;color:var(--muted2);">contratos</div>
                    <div th:if="${ganancias != null and ganancias > 0}" style="font-size:11px;color:var(--amber2);margin-top:2px;" th:text="'$' + ${#numbers.formatInteger(ganancias,1,'COMMA')}"></div>
                  </div>
                </td>''',
    '''                <td>
                  <div th:with="contratos=${contratacionesPorArtista[m.id]},ganancias=${gananciasPorArtista[m.id]}"
                       style="display:flex;align-items:center;gap:16px;">
                    <div>
                      <div style="font-family:var(--serif);font-size:1.1rem;font-weight:600;" th:text="${contratos != null ? contratos : 0}"></div>
                      <div style="font-size:11px;color:var(--muted2);">contratos</div>
                    </div>
                    <div th:if="${ganancias != null and ganancias > 0}"
                         style="padding:4px 10px;background:rgba(200,129,58,0.1);border:1px solid rgba(200,129,58,0.2);border-radius:8px;">
                      <div style="font-family:var(--serif);font-size:1rem;font-weight:600;color:var(--amber2);" th:text="'$' + ${#numbers.formatInteger(ganancias,1,'COMMA')}"></div>
                      <div style="font-size:10px;color:var(--muted2);">ganado</div>
                    </div>
                  </div>
                </td>'''
)

# Agregar columna fecha en solicitudes pendientes
content = content.replace(
    '<thead><tr><th>Cliente</th><th>Artista</th><th>Obra</th><th>Aceptar</th><th>Rechazar</th></tr></thead>',
    '<thead><tr><th>Cliente</th><th>Artista</th><th>Obra</th><th>Fecha solicitada</th><th>Aceptar</th><th>Rechazar</th></tr></thead>'
)

content = content.replace(
    '''                <td>
                  <span th:if="${c.obraNombre != null}" class="badge bdg-art" th:text="${c.obraNombre}"></span>
                  <span th:unless="${c.obraNombre != null}" style="color:var(--muted);font-size:13px;">—</span>
                </td>
                <td>
                  <form th:action="@{/admin/aceptar/{id}(id=${c.id})}" method="post"''',
    '''                <td>
                  <span th:if="${c.obraNombre != null}" class="badge bdg-art" th:text="${c.obraNombre}"></span>
                  <span th:unless="${c.obraNombre != null}" style="color:var(--muted);font-size:13px;">—</span>
                </td>
                <td>
                  <span th:if="${c.fechaEvento != null}" style="font-family:var(--serif);font-size:0.95rem;" th:text="${#temporals.format(c.fechaEvento,'dd MMM yyyy')}"></span>
                  <span th:unless="${c.fechaEvento != null}" style="color:var(--muted);font-size:13px;">—</span>
                </td>
                <td>
                  <form th:action="@{/admin/aceptar/{id}(id=${c.id})}" method="post"'''
)

open('src/main/resources/templates/admin/dashboard.html', 'w', encoding='utf-8').write(content)
print('Dashboard listo')
