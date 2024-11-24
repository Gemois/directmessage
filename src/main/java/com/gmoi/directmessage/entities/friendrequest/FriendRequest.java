package com.gmoi.directmessage.entities.friendrequest;

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
public class FriendRequest extends Auditable {
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

}
