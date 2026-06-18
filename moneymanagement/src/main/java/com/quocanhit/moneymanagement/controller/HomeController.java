package com.quocanhit.moneymanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping({"/api/v1/health", "/api/v1/status"})
    public String healthyCheck() {
        return "Application is running...";
    }


    @GetMapping("/api/v1/test")
    public String test() {
        return "Application is running test Authorization...";
    }
}
