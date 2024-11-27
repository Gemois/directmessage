package com.gmoi.directmessage.models;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
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