content = open('src/main/java/com/XPilot/XPilot/models/usuario.java', encoding='utf-8').read()
content = content.replace(
    '// 🔔 TOKEN FIREBASE',
    '// ✅ VERIFICACION EMAIL\n    @Column(name = "verificado")\n    private boolean verificado = false;\n\n    @Column(name = "token_verificacion", length = 100)\n    private String tokenVerificacion;\n\n    // 🔔 TOKEN FIREBASE'
)
content = content.replace(
    'public String getFcmToken()',
    'public boolean isVerificado() { return verificado; }\n    public void setVerificado(boolean verificado) { this.verificado = verificado; }\n    public String getTokenVerificacion() { return tokenVerificacion; }\n    public void setTokenVerificacion(String tokenVerificacion) { this.tokenVerificacion = tokenVerificacion; }\n\n    public String getFcmToken()'
)
open('src/main/java/com/XPilot/XPilot/models/usuario.java', 'w', encoding='utf-8').write(content)
print('Listo')
