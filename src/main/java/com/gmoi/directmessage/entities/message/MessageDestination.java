package com.gmoi.directmessage.entities.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageDestination {

    MESSAGES("/queue/messages"),
    TYPING("/queue/typing"),
    READ("/queue/read"),
    STATUS("/queue/status");

    private final String destination;

}
