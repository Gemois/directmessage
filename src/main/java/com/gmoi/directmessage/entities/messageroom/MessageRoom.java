package com.gmoi.directmessage.entities.messageroom;

import com.gmoi.directmessage.entities.Auditable;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MessageRoom extends Auditable {
    @Id
    @GeneratedValue()
    private Long id;
    private String chatId;
    private String senderId;
    private String recipientId;
}