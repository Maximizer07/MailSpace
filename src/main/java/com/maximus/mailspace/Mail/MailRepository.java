package com.maximus.mailspace.Mail;

import com.maximus.mailspace.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailRepository extends JpaRepository<Mail, Integer> {
    List<Mail> findByRecipientAndStaredOrderByIdDesc(User recipient, boolean star);
    List<Mail> findByRecipientAndTopicContainingIgnoreCase(User recipient, String search_phrase);
    List<Mail> findBySenderOrderByIdDesc(User sender);
    List<Mail> findByRecipientOrderByIdDesc(User sender);
    List<Mail> findByRecipientAndImportantOrderByIdDesc(User recipient, boolean important);
}
