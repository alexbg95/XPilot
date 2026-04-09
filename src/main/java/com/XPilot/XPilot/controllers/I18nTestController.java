package com.XPilot.XPilot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-i18n")
public class I18nTestController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        String message = messageSource.getMessage(
            "welcome.user", 
            new Object[]{"Juan"}, 
            LocaleContextHolder.getLocale()
        );

        return ResponseEntity.ok(message);
    }

    @GetMapping("/error")
    public ResponseEntity<String> error() {
        String message = messageSource.getMessage(
            "error.internal",
            null,
            LocaleContextHolder.getLocale()
        );

        return ResponseEntity.ok(message);
    }
}
