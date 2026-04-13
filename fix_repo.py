content = open('src/main/java/com/XPilot/XPilot/repositories/ContratacionRepository.java', encoding='utf-8').read()
if 'countByArtista_Id' not in content:
    content = content.replace(
        'List<Contratacion> findByArtista_Usuario_Id(Long usuarioId);',
        'List<Contratacion> findByArtista_Usuario_Id(Long usuarioId);\n    long countByArtista_Id(Long artistaId);'
    )
    open('src/main/java/com/XPilot/XPilot/repositories/ContratacionRepository.java', 'w', encoding='utf-8').write(content)
print('Repo listo')
