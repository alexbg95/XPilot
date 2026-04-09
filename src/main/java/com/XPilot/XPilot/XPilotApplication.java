package com.XPilot.XPilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.XPilot.XPilot")
public class XPilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(XPilotApplication.class, args);
    }
}