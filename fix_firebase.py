content = open('src/main/java/com/XPilot/XPilot/config/FirebaseConfig.java', encoding='utf-8').read()
content = content.replace(
    'import java.io.ByteArrayInputStream;',
    'import java.io.ByteArrayInputStream;\nimport java.util.Base64;'
)
content = content.replace(
    'String firebaseJson = System.getenv("FIREBASE_CREDENTIALS");',
    'String b64 = System.getenv("FIREBASE_CREDENTIALS_B64");\n            String firebaseJson = (b64 != null && !b64.isBlank()) ? new String(Base64.getDecoder().decode(b64)) : System.getenv("FIREBASE_CREDENTIALS");'
)
open('src/main/java/com/XPilot/XPilot/config/FirebaseConfig.java', 'w', encoding='utf-8').write(content)
print('Listo')
