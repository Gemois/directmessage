package com.gmoi.directmessage.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadNotification {
    private String chatId;
    private String recipientId;
    private String senderId;
    private LocalDateTime readUpToDate;
}
