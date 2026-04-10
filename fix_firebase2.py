content = open('src/main/java/com/XPilot/XPilot/config/FirebaseConfig.java', encoding='utf-8').read()
content = content.replace(
    'credentialsStream = new ByteArrayInputStream(\n                    firebaseJson.getBytes(StandardCharsets.UTF_8)\n                );',
    'String fixedJson = firebaseJson.replace("\\\\n", "\\n");\n                credentialsStream = new ByteArrayInputStream(\n                    fixedJson.getBytes(StandardCharsets.UTF_8)\n                );'
)
open('src/main/java/com/XPilot/XPilot/config/FirebaseConfig.java', 'w', encoding='utf-8').write(content)
print('Listo')
