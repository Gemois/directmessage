package com.gmoi.directmessage.models;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;


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
