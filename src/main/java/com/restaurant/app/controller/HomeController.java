package com.restaurant.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Restaurant Backend is Running 🚀";
    }

    @GetMapping("/api/health")
    public String health() {
        return "Backend Health OK ✅";
    }
}