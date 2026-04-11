content = open('src/main/resources/templates/admin/dashboard.html', encoding='utf-8', errors='ignore').read()
content = content.replace(
    '<button class="xb xb-ghost xb-sm tab-btn" onclick="filtrar(\'RECHAZADO\',this)">Rechazados</button>',
    '<button class="xb xb-ghost xb-sm tab-btn" onclick="filtrar(\'RECHAZADO\',this)">Rechazados</button>\n            <button class="xb xb-ghost xb-sm tab-btn" onclick="filtrar(\'FINALIZADO\',this)" style="color:#a0a0ff;border-color:rgba(100,100,255,0.3);">Finalizados</button>'
)
open('src/main/resources/templates/admin/dashboard.html', 'w', encoding='utf-8').write(content)
print('Listo')
