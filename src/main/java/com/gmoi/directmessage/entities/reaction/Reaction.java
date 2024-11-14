package com.gmoi.directmessage.entities.reaction;

import com.gmoi.directmessage.entities.message.Message;
import com.gmoi.directmessage.entities.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
public class Reaction {
    @Id
    @GeneratedValue()
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Message message;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String emoji;

    @CreatedDate
    private LocalDateTime timestamp;
}
