content = open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', encoding='utf-8').read()
content = content.replace(
    'ObjectUtils.asMap("folder", "xpilot/perfiles", "upload_preset", "ml_default")',
    'ObjectUtils.asMap("folder", "xpilot/perfiles")'
)
content = content.replace(
    'ObjectUtils.asMap("folder", "xpilot/obras", "upload_preset", "ml_default")',
    'ObjectUtils.asMap("folder", "xpilot/obras")'
)
content = content.replace(
    'ObjectUtils.asMap("folder", "xpilot/obras-extra", "upload_preset", "ml_default")',
    'ObjectUtils.asMap("folder", "xpilot/obras-extra")'
)
open('src/main/java/com/XPilot/XPilot/controllers/AdminController.java', 'w', encoding='utf-8').write(content)
print('Listo')
