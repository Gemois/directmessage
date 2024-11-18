package com.gmoi.directmessage.entities.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PinnedNotification {
    private String messageId;
    private boolean isPinned;
}
