package com.gmoi.directmessage.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PinnedNotification {
    private String messageId;
    private boolean isPinned;
}
