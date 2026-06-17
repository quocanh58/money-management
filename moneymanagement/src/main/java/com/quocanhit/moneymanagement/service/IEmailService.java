package com.quocanhit.moneymanagement.service;

import org.springframework.stereotype.Service;

@Service
public interface IEmailService {
    void sendEmail(String to, String subject, String body);
}
