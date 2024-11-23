package com.gmoi.directmessage.entities.reaction;

import com.gmoi.directmessage.entities.Auditable;
import com.gmoi.directmessage.entities.message.Message;
import com.gmoi.directmessage.entities.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Reaction extends Auditable {
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

}
