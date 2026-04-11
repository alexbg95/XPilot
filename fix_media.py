content = open('src/main/java/com/XPilot/XPilot/models/media.java', encoding='utf-8').read()
content = content.replace(
    '@Column(length = 1000)\n    private String biografia;',
    '@Column(length = 1000)\n    private String biografia;\n\n    private Double precio;\n\n    private boolean disponible = true;'
)
content = content.replace(
    'public usuario getUsuario() { return usuario; }',
    'public Double getPrecio() { return precio; }\n    public void setPrecio(Double precio) { this.precio = precio; }\n    public boolean isDisponible() { return disponible; }\n    public void setDisponible(boolean disponible) { this.disponible = disponible; }\n    public usuario getUsuario() { return usuario; }'
)
open('src/main/java/com/XPilot/XPilot/models/media.java', 'w', encoding='utf-8').write(content)
print('Listo')
