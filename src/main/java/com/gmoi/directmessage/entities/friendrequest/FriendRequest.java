package com.gmoi.directmessage.entities.friendrequest;

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

public class FriendRequest {
    @Id
    @GeneratedValue()
    private Long requestId;

    @ManyToOne
    @JoinColumn()
    private User sender;

    @ManyToOne
    @JoinColumn()
    private User recipient;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

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
