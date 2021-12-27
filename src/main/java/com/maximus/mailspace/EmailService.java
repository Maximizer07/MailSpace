package com.maximus.mailspace;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;

@Service
@Transactional
@AllArgsConstructor
public class EmailService {

    private JavaMailSender javaMailSender;
    public void sendmail(SimpleMailMessage email) throws AddressException, MessagingException, IOException {
        javaMailSender.send(email);
    }
}

