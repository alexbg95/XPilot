content = open('src/main/java/com/XPilot/XPilot/models/MediaFoto.java', encoding='utf-8').read()
content = content.replace(
    '    @Column(length = 200)\n    private String nombre;',
    '    @Column(length = 200)\n    private String nombre;\n\n    private Double precio;'
)
content = content.replace(
    'public MediaFoto(String url, String nombre, media media) {\n        this.url = url;\n        this.nombre = nombre;\n        this.media = media;\n    }',
    'public MediaFoto(String url, String nombre, media media) {\n        this.url = url;\n        this.nombre = nombre;\n        this.media = media;\n    }\n    public MediaFoto(String url, String nombre, Double precio, media media) {\n        this.url = url;\n        this.nombre = nombre;\n        this.precio = precio;\n        this.media = media;\n    }'
)
content = content.replace(
    'public void setMedia(media media) { this.media = media; }',
    'public void setMedia(media media) { this.media = media; }\n    public Double getPrecio() { return precio; }\n    public void setPrecio(Double precio) { this.precio = precio; }'
)
open('src/main/java/com/XPilot/XPilot/models/MediaFoto.java', 'w', encoding='utf-8').write(content)
print('Listo')
