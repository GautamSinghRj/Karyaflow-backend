package com.example.karyaflow.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendMailWithAttachment(String email,String subject,String body)
    {
        try {
            MimeMessage mimeMessage=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);//here true = you can add attachments
            helper.setFrom("karyaflow@gmail.com");
            helper.setTo(email);//receiver mail
            helper.setSubject(subject);
            helper.setText(body,true);//here true = sending body as html
            mailSender.send(mimeMessage);
        }
        catch (MessagingException e) {
            throw new RuntimeException("Failed to send email",e);
        }
    }
}
