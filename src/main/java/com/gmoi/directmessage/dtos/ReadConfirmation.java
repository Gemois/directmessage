package com.gmoi.directmessage.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReadConfirmation {
    private String chatId;
    private String recipientId;
    private LocalDateTime readUpToDate;
}