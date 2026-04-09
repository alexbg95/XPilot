package com.XPilot.XPilot.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.XPilot.XPilot.repositories.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private usuarioRepository usuarioRepo;

    public void enviarNotificacion(String token, String titulo, String mensaje) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(
                            Notification.builder()
                                    .setTitle(titulo)
                                    .setBody(mensaje)
                                    .build()
                    )
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("🔥 Notificación enviada: " + response);

        } catch (FirebaseMessagingException e) {
            String errorCode = e.getMessagingErrorCode() != null ? e.getMessagingErrorCode().name() : "UNKNOWN";
            System.out.println("❌ Error Firebase [" + errorCode + "]: " + e.getMessage());

            // Si el token es inválido o está vencido, lo eliminamos de la BD
            if (errorCode.equals("UNREGISTERED") || errorCode.equals("INVALID_ARGUMENT")) {
                System.out.println("🧹 Token inválido, limpiando de la BD...");
                usuarioRepo.findAll().forEach(u -> {
                    if (token.equals(u.getFcmToken())) {
                        u.setFcmToken(null);
                        usuarioRepo.save(u);
                        System.out.println("✅ Token limpiado para: " + u.getEmail());
                    }
                });
            }

        } catch (Exception e) {
            System.out.println("❌ Error enviando notificación: " + e.getMessage());
        }
    }
}
