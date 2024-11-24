package com.gmoi.directmessage.auth.confirmation;

import com.gmoi.directmessage.entities.Auditable;
import com.gmoi.directmessage.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConfirmationToken extends Auditable {
    @Id
    @GeneratedValue()
    private Long id;

    @Column(nullable = false)
    private String token;

    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn()
    private User user;
}