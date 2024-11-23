package com.gmoi.directmessage.entities.messageroom;

import com.gmoi.directmessage.entities.Auditable;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class MessageRoom extends Auditable {
    @Id
    @GeneratedValue()
    private Long id;
    private String chatId;
    private String senderId;
    private String recipientId;
}