package com.quocanhit.moneymanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/health", "/status"})
public class HomeController {

    @GetMapping()
    public String healthyCheck() {
        return "Application is running...";
    }
}
