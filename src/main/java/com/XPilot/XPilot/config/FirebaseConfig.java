package com.XPilot.XPilot.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@org.springframework.context.annotation.Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() {
        try {
            if (!FirebaseApp.getApps().isEmpty()) return;

            InputStream credentialsStream;

            // En Railway: usa variable de entorno FIREBASE_CREDENTIALS
            String firebaseJson = System.getenv("FIREBASE_CREDENTIALS");

            if (firebaseJson != null && !firebaseJson.isBlank()) {
                // Producción: leer desde variable de entorno
                credentialsStream = new ByteArrayInputStream(
                    firebaseJson.getBytes(StandardCharsets.UTF_8)
                );
                System.out.println("✅ Firebase: credenciales desde variable de entorno");
            } else {
                // Local: leer desde archivo
                credentialsStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("firebase-service-account.json");
                System.out.println("✅ Firebase: credenciales desde archivo local");
            }

            if (credentialsStream == null) {
                System.out.println("⚠️ Firebase: no se encontraron credenciales — notificaciones push desactivadas");
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                .build();

            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase inicializado correctamente");

        } catch (Exception e) {
            System.out.println("❌ Error inicializando Firebase: " + e.getMessage());
        }
    }
}
