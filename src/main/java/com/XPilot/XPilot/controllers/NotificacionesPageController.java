package com.XPilot.XPilot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificacionesPageController {

    @GetMapping("/notificaciones")
    public String notificaciones() {
        return "notificaciones";
    }
}
