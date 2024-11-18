package com.gmoi.directmessage.entities.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadNotification {
    private String chatId;
    private String recipientId;
    private String senderId;
    private LocalDateTime readUpToDate;
}
