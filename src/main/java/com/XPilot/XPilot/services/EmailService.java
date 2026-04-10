package com.XPilot.XPilot.services;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${app.base-url}")
    private String baseUrl;

    public void enviarConfirmacion(String email, String token) {
        try {
            Resend resend = new Resend(apiKey);
            String link = baseUrl + "/verificar?token=" + token;
            String html = "<div style='font-family:sans-serif;max-width:480px;margin:0 auto;background:#0c0c0c;padding:2rem;border-radius:12px;'>"
                + "<h2 style='color:#e8a050;'>Bienvenido a XPilot</h2>"
                + "<p style='color:#c4bdb4;'>Confirma tu correo haciendo clic en el boton:</p>"
                + "<a href='" + link + "' style='display:inline-block;margin:1rem 0;padding:12px 28px;background:#c8813a;color:#000;border-radius:8px;text-decoration:none;font-weight:700;'>Confirmar cuenta</a>"
                + "<p style='color:#5a5550;font-size:12px;'>Si no creaste esta cuenta, ignora este mensaje.</p>"
                + "</div>";

            CreateEmailOptions req = CreateEmailOptions.builder()
                .from("XPilot <onboarding@resend.dev>")
                .to(email)
                .subject("Confirma tu cuenta en XPilot")
                .html(html)
                .build();

            resend.emails().send(req);
            System.out.println("Email enviado a: " + email);
        } catch (Exception e) {
            System.out.println("Error enviando email: " + e.getMessage());
        }
    }
}
