package com.gmoi.directmessage.entities.friendship;

import com.gmoi.directmessage.entities.user.User;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Friendship {

    @Id
    @GeneratedValue()
    private Long friendshipId;

    @ManyToOne
    @JoinColumn()
    private User user1;

    @ManyToOne
    @JoinColumn()
    private User user2;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

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
