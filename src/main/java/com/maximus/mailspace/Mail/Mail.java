package com.maximus.mailspace.Mail;

import com.maximus.mailspace.User.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mails")
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column(name = "topic", nullable = false, length = 64)
    private String topic;

    @Column(name = "body", nullable = false, length = 250)
    private String body;

    @Column(name = "date_of_sending", nullable = false, length = 250)
    private LocalDate sendingDate;

    @Column(name = "stared", nullable = false)
    private Boolean stared = false;

    @Column(name = "important", nullable = false)
    private Boolean important = false;

    @Column(name = "readed", nullable = false)
    private Boolean readed = false;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    public String getShortBody() {
        if (body.length() > 50) return body.substring(0, 50)+ "...";
        else {
            return body;
        }
    }
}
