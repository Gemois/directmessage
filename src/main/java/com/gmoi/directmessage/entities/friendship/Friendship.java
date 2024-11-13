package com.gmoi.directmessage.entities.friendship;

import com.gmoi.directmessage.entities.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
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

    @CreatedDate
    private LocalDateTime createdAt;

}
