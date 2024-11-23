package com.gmoi.directmessage.entities.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadConfirmation {
    private String chatId;
    private String recipientId;
    private LocalDateTime readUpToDate;
}