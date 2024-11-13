package com.gmoi.directmessage.entities.friendrequest;

import com.gmoi.directmessage.entities.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
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

    @CreatedDate
    private LocalDateTime createdAt;
}
