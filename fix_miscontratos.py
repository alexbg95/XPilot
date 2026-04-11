content = open('src/main/resources/templates/mis-contratos.html', encoding='utf-8', errors='ignore').read()
content = content.replace(
    '              <span th:if="${c.estado == \'PENDIENTE\'}"  class="badge bdg-pending">Pendiente</span>\n              <span th:if="${c.estado == \'ACEPTADO\'}"   class="badge bdg-ok">Aceptado</span>\n              <span th:if="${c.estado == \'RECHAZADO\'}"  class="badge bdg-no">Rechazado</span>',
    '              <span th:if="${c.estado == \'PENDIENTE\'}"  class="badge bdg-pending">Pendiente</span>\n              <span th:if="${c.estado == \'ACEPTADO\'}"   class="badge bdg-ok">Aceptado</span>\n              <span th:if="${c.estado == \'RECHAZADO\'}"  class="badge bdg-no">Rechazado</span>\n              <span th:if="${c.estado == \'FINALIZADO\'}" class="badge" style="background:rgba(100,100,255,0.15);color:#a0a0ff;border:1px solid rgba(100,100,255,0.2);">Finalizado</span>'
)
open('src/main/resources/templates/mis-contratos.html', 'w', encoding='utf-8').write(content)
print('Listo')
