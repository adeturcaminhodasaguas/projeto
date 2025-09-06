package br.com.caminhodasaguas.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Value("${spring.mail.email}")
    private String email;

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String reciever, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(reciever);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

}
