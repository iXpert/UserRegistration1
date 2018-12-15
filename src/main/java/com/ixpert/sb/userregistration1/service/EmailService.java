package com.ixpert.sb.userregistration1.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender1){
        this.javaMailSender = javaMailSender1;
    }

    @Async
    public void sendEmail(SimpleMailMessage simpleMailMessage){
        javaMailSender.send(simpleMailMessage);
    }
}
