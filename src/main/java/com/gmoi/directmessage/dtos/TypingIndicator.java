package com.gmoi.directmessage.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypingIndicator {
    private String sender;
    private boolean isTyping;
    private String recipient;
}
