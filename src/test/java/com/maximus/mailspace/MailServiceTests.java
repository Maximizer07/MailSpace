package com.maximus.mailspace;

import com.maximus.mailspace.Mail.Mail;
import com.maximus.mailspace.Mail.MailRepository;
import com.maximus.mailspace.Mail.MailService;

import com.maximus.mailspace.User.User;
import com.maximus.mailspace.User.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MailServiceTests {
    @InjectMocks
    private MailService mailService;
    @Mock
    private MailRepository mailRepository;
    @Captor
    ArgumentCaptor<Mail> captor;

    private Mail mail1, mail2, mail3, mail4;
    private User user1, user2;
    @BeforeEach
    void init() {

        user1 = new User();
        user1.setUsername("test1@mailspace.ru");
        user1.setId(1L);
        user1.setFirstname("Ivan");
        user1.setEmail("Ivan@gmail.com");
        user1.setLastname("Ivanov");
        user1.setPassword("123456");
        user1.setUserRole(UserRole.USER);

        user2 = new User();
        user2.setUsername("test2@mailspace.ru");
        user2.setId(2L);
        user2.setFirstname("Maxim");
        user2.setEmail("Maxim@gmail.com");
        user2.setLastname("Maximov");
        user2.setPassword("123456");
        user2.setUserRole(UserRole.USER);

        mail1 = new Mail();
        mail1.setId(1);
        mail1.setTopic("Test1");
        mail1.setBody("Body1");
        mail1.setSender(user1);
        mail1.setRecipient(user2);
        mail1.setImportant(true);

        mail2 = new Mail();
        mail2.setId(2);
        mail2.setTopic("Test2");
        mail2.setBody("Body2");
        mail2.setSender(user2);
        mail2.setRecipient(user1);
        mail2.setStared(true);

        mail3 = new Mail();
        mail3.setId(3);
        mail3.setTopic("Test3");
        mail3.setBody("Body3");
        mail3.setSender(user1);
        mail3.setRecipient(user2);
        mail3.setImportant(true);

        mail4 = new Mail();
        mail4.setId(4);
        mail4.setTopic("Test4");
        mail4.setBody("Body4");
        mail4.setSender(user2);
        mail4.setRecipient(user1);
        mail4.setStared(true);
    }
    @Test
    void getMailbyId() {
        Mockito.when(mailService.findById(1)).thenReturn(java.util.Optional.of(mail1));
        Mockito.when(mailService.findById(2)).thenReturn(java.util.Optional.of(mail2));
        Mockito.when(mailService.findById(3)).thenReturn(java.util.Optional.of(mail3));
        assertEquals(java.util.Optional.of(mail1), mailService.findById(1));
        assertEquals(java.util.Optional.of(mail2), mailService.findById(2));
        assertEquals(java.util.Optional.of(mail3), mailService.findById(3));
    }
    @Test
    void findSenderMail() {
        Mockito.when(mailService.findSenderMail(user1)).thenReturn(List.of(mail1, mail3));
        assertEquals(List.of(mail1, mail3), mailService.findSenderMail(user1));
    }
    @Test
    void findRecipientMail() {
        Mockito.when(mailService.findRecipientMail(user1)).thenReturn(List.of(mail2, mail4));
        assertEquals(List.of(mail2, mail4), mailService.findRecipientMail(user1));
    }
    @Test
    void findStared() {
        Mockito.when(mailService.findStared(user1,true)).thenReturn(List.of(mail2, mail4));
        assertEquals(List.of(mail2, mail4), mailService.findStared(user1, true));
    }
    @Test
    void findImportant() {
        Mockito.when(mailService.findImportant(user1,true)).thenReturn(List.of());
        assertEquals(List.of(), mailService.findImportant(user1, true));
    }
    @Test
    void search() {
        Mockito.when(mailService.findMail(user1,"Te")).thenReturn(List.of(mail2, mail4));
        assertEquals(List.of(mail2, mail4), mailService.findMail(user1,"Te"));
    }
    @Test
    void saveMail() {
        mailService.saveMail(mail4);
        Mockito.verify(mailRepository).save(captor.capture());
        Mail captured = captor.getValue();
        assertEquals("Test4", captured.getTopic());
    }
    @Test
    void deleteMail() {
        mailService.deleteMail(1);
        Mockito.verify(mailRepository).deleteById(1);
    }
}
