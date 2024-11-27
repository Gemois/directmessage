package com.gmoi.directmessage.models;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
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
