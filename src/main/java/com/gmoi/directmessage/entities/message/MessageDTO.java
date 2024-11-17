package com.gmoi.directmessage.entities.message;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

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
    private Date timestamp;
}
