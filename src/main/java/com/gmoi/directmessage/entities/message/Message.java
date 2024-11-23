package com.gmoi.directmessage.entities.message;

import com.gmoi.directmessage.entities.Auditable;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Message extends Auditable {
    @Id
    @GeneratedValue()
    private Long id;
    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private boolean isAttachmentMsg;
    private String attachment;

    private boolean read;
    private LocalDateTime readAt;
    private boolean isEdited;
    private LocalDateTime editedAt;
    private boolean isDeleted;
    private LocalDateTime deletedAt;
    private boolean isPinned = false;

}