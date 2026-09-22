package com.banfico.banking.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/app")
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<String> getAppStatus() {
        return  ResponseEntity.ok("Application is running");
    }
    
    @GetMapping("/info")
    public Map<String, String> getAppInfo() {
        return Map.of(
            "name", "Banking App",
            "version", "1.0.0",
            "description", "A simple banking application"
        );
    }
    
}
