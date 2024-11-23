package com.gmoi.directmessage.entities.reaction;

import com.gmoi.directmessage.entities.message.Message;
import com.gmoi.directmessage.entities.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Long lastModifiedBy;

    @Version
    private long version;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modifiedDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
