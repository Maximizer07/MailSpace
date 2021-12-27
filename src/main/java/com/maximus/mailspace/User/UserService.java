package com.maximus.mailspace.User;

import com.maximus.mailspace.ConfirmationToken.ConfirmationToken;
import com.maximus.mailspace.ConfirmationToken.ConfirmationTokenService;
import com.maximus.mailspace.EmailService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserService implements UserDetailsService {

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private EmailService emailSenderService;


    @SneakyThrows
    void sendConfirmationMail(String userMail, String token) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userMail);
        mailMessage.setSubject("Ссылка для подтверждения почты восстановления на Mailspace!");
        mailMessage.setFrom("<MAIL>");
        mailMessage.setText(
                "Спасибо, что зарегистрировались на нашем сайте. Пожалуйста, перейдите по ссылке для активации вашей почты для восстановления. " + "http://localhost:8080/sign-up/confirm?token="
                        + token);
        emailSenderService.sendmail(mailMessage);
    }


    public boolean uniqueEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }
    public boolean uniqueUsername(String username){
        return userRepository.findByUsername(username).isPresent();
    }
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("com.maximus.mailapp.User.User with email {0} cannot be found.", username)));
    }


    public void signUpUser(User user) {
        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setUserRole(UserRole.USER);
        user.setEnabled(true);
        final User createdUser = userRepository.save(user);
        if (!user.getEmail().isEmpty()) {
            System.out.println("Mail = :" + user.getEmail());
            final ConfirmationToken confirmationToken = new ConfirmationToken(user);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
        }
    }

    public void confirmUser(ConfirmationToken confirmationToken) {
        final User user = confirmationToken.getUser();
        user.setConfirmed(true);
        userRepository.save(user);
        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
    }

    public List<User> readAll() {
        return userRepository.findAll();
    }

    public void updateFirstName(Long id, String name) {
        Optional<User> u = userRepository.findById(id);
        User uu = u.get();
        uu.setFirstname(name);
        userRepository.save(uu);
    }

    public void updateSurName(Long id, String surname) {
        Optional<User> u = userRepository.findById(id);
        User uu = u.get();
        uu.setLastname(surname);
        userRepository.save(uu);
    }

    public void deleteUser(Long id){
        userRepository.deleteUserById(id);
    }
}
