package com.XPilot.XPilot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    public void enviarConfirmacion(String email, String token) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Confirma tu cuenta en XPilot");
            String link = baseUrl + "/verificar?token=" + token;
            String html = "<div style='font-family:sans-serif;max-width:480px;margin:0 auto;background:#0c0c0c;padding:2rem;border-radius:12px;'>"
                + "<h2 style='color:#e8a050;'>Bienvenido a XPilot</h2>"
                + "<p style='color:#c4bdb4;'>Confirma tu correo haciendo clic en el botón:</p>"
                + "<a href='" + link + "' style='display:inline-block;margin:1rem 0;padding:12px 28px;background:#c8813a;color:#000;border-radius:8px;text-decoration:none;font-weight:700;'>Confirmar cuenta</a>"
                + "<p style='color:#5a5550;font-size:12px;'>Si no creaste esta cuenta, ignora este mensaje.</p>"
                + "</div>";
            helper.setText(html, true);
            mailSender.send(msg);
            System.out.println("Email enviado a: " + email);
        } catch (Exception e) {
            System.out.println("Error enviando email: " + e.getMessage());
        }
    }
}
