package com.gmoi.directmessage.entities.friendship;

import com.gmoi.directmessage.entities.Auditable;
import com.gmoi.directmessage.entities.user.User;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Friendship extends Auditable {

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

}
