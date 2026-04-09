package com.XPilot.XPilot.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    public void enviarNotificacion(String token, String titulo, String mensaje) {

        try {

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(titulo)
                            .setBody(mensaje)
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);

            System.out.println("✅ Notificación enviada: " + response);

        } catch (Exception e) {
            System.out.println("❌ Error enviando notificación: " + e.getMessage());
        }
    }
}