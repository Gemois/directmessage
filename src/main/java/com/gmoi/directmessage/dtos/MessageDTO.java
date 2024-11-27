package com.gmoi.directmessage.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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
