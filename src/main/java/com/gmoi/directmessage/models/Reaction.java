package com.gmoi.directmessage.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
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
