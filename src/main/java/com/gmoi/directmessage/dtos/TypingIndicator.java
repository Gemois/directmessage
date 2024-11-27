package com.gmoi.directmessage.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypingIndicator {
    private String sender;
    private boolean isTyping;
    private String recipient;
}
