content = open('src/main/resources/templates/admin/dashboard.html', encoding='utf-8', errors='ignore').read()

content = content.replace(
    '<div class="col-6 col-md-3"><div class="stat"><div class="stat-icon">📋</div><div class="stat-val" th:text="${#lists.size(todos)}">0</div><div class="stat-lbl">Total</div></div></div>',
    '<div class="col-6 col-md-3"><div class="stat"><div class="stat-icon">📋</div><div class="stat-val" th:text="${#lists.size(todos)}">0</div><div class="stat-lbl">Total</div></div></div>\n        <div class="col-6 col-md-3"><div class="stat"><div class="stat-icon">💰</div><div class="stat-val" style="font-size:1.3rem;" th:text="\'$\' + ${#numbers.formatInteger(gananciasPorArtista.values().stream().mapToDouble(v -> v).sum(),1,\'COMMA\')}">$0</div><div class="stat-lbl">Ganancias</div></div></div>'
)

open('src/main/resources/templates/admin/dashboard.html', 'w', encoding='utf-8').write(content)
print('Listo')
