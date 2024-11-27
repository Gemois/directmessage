package com.gmoi.directmessage.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReadNotification {
    private String chatId;
    private String recipientId;
    private String senderId;
    private LocalDateTime readUpToDate;
}
