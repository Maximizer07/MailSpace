package com.maximus.mailspace;

import com.maximus.mailspace.Mail.Mail;
import com.maximus.mailspace.User.User;
import com.maximus.mailspace.User.UserRepository;
import com.maximus.mailspace.User.UserRole;
import com.maximus.mailspace.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Captor
    ArgumentCaptor<User> captor;
    private User user1, user2, user3;

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

        user3 = new User();
        user3.setUsername("test3@mailspace.ru");
        user3.setId(3L);
        user3.setFirstname("Petr");
        user3.setEmail("Petr@gmail.com");
        user3.setLastname("Petrov");
        user3.setPassword("123456");
        user3.setUserRole(UserRole.USER);
    }
    @Test
    void getUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user1,
                user2,user3));
        assertEquals(3,
                userService.readAll().size());
        assertEquals("Ivan",
                userRepository.findAll().get(0).getFirstname());
    }
    @Test
    void userExistsbyName() {
        Mockito.when(userRepository.findByUsername("test1@mailspace.ru")).thenReturn(java.util.Optional.of(user1));
        assertEquals("Ivan",
                userService.loadUserByUsername("test1@mailspace.ru").getFirstname());
    }
    @Test
    void userExistsbyUsername() {
        Mockito.when(userRepository.findByUsername("test2@mailspace.ru")).thenReturn(java.util.Optional.of(user2));
        assertEquals("test2@mailspace.ru",
                userService.loadUserByUsername("test2@mailspace.ru").getUsername());
    }
    @Test
    void updateUserName() {
        Mockito.when(userRepository.findByUsername("test2@mailspace.ru")).thenReturn(java.util.Optional.of(user2));
        assertEquals("Maxim",
                userService.loadUserByUsername("test2@mailspace.ru").getFirstname());
        userService.loadUserByUsername("test2@mailspace.ru").setFirstname("Maksim");
        assertEquals("Maksim",
                userService.loadUserByUsername("test2@mailspace.ru").getFirstname());
    }
    @Test
    void updateUserSurName() {
        Mockito.when(userRepository.findByUsername("test2@mailspace.ru")).thenReturn(java.util.Optional.of(user2));
        assertEquals("Maximov",
                userService.loadUserByUsername("test2@mailspace.ru").getLastname());
        userService.loadUserByUsername("test2@mailspace.ru").setLastname("Maksimov");
        assertEquals("Maksimov",
                userService.loadUserByUsername("test2@mailspace.ru").getLastname());
    }
    @Test
    void updateUserRole() {
        Mockito.when(userRepository.findByUsername("test2@mailspace.ru")).thenReturn(java.util.Optional.of(user2));
        assertEquals(UserRole.USER,
                userService.loadUserByUsername("test2@mailspace.ru").getUserRole());
        userService.loadUserByUsername("test2@mailspace.ru").setUserRole(UserRole.ADMIN);
        assertEquals(UserRole.ADMIN,
                userService.loadUserByUsername("test2@mailspace.ru").getUserRole());
    }
    @Test
    void userDelete() {
        Mockito.when(userRepository.findByUsername("test3@mailspace.ru")).thenReturn(java.util.Optional.of(user3));
        assertEquals("Petr",
                userService.loadUserByUsername("test3@mailspace.ru").getFirstname());
        userService.deleteUser(3L);
        Mockito.verify(userRepository).deleteUserById(3L);
        assertEquals(0,userService.readAll().size());

    }
    @Test
    void saveUser() {
        userRepository.save(user2);
        Mockito.verify(userRepository).save(captor.capture());
        User captured = captor.getValue();
        assertEquals("test2@mailspace.ru", captured.getUsername());
    }
}
