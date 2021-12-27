package com.maximus.mailspace.Mail;

import com.maximus.mailspace.User.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MailService {

    @Autowired
    private MailRepository mailRepository;

    public List<Mail> findStared(User recipient, boolean star){
        return mailRepository.findByRecipientAndStaredOrderByIdDesc(recipient, star);
    }

    public Optional<Mail> findById(int id) {
        return mailRepository.findById(id);
    }

    public void setReaded(Mail mail){
        mail.setReaded(Boolean.TRUE);
        mailRepository.save(mail);
    }

    public void setImportant(Mail mail){
        Boolean bool = mail.getImportant();
        mail.setImportant(!bool);
        mailRepository.save(mail);
    }

    public void setStared(Mail mail){
        Boolean bool = mail.getStared();
        mail.setStared(!bool);
        mailRepository.save(mail);
    }

    public List<Mail> findMail(User user, String search_phrase){
        return mailRepository.findByRecipientAndTopicContainingIgnoreCase(user, search_phrase);
    }
    public List<Mail> findSenderMail(User user){
        return mailRepository.findBySenderOrderByIdDesc(user);
    }

    public List<Mail> findRecipientMail(User user){
        return mailRepository.findByRecipientOrderByIdDesc(user);
    }

    public void deleteMail(int id) {
        mailRepository.deleteById(id);
    }

    public void saveMail(Mail mail) {
        mailRepository.save(mail);
    }

    public List<Mail> findImportant(User recipient, boolean important){
        return mailRepository.findByRecipientAndImportantOrderByIdDesc(recipient, important);
    }

}
