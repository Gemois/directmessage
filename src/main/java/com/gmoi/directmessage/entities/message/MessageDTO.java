package com.gmoi.directmessage.entities.message;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class MessageDTO {
    private Long id;
    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private String attachment;
    private LocalDateTime createdAt;
    private boolean read;
    private LocalDateTime readAt;
    private boolean isEdited;
    private LocalDateTime editedAt;
    private boolean isDeleted;
    private LocalDateTime deletedAt;
    private boolean isPinned;
}
