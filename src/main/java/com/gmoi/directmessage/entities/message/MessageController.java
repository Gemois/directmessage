package com.gmoi.directmessage.entities.message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService chatMessageService;

    @MessageMapping("/send-message")
    public void sendMessage(@Payload Message message) {
        Message savedMsg = chatMessageService.save(message);
        messagingTemplate.convertAndSendToUser(message.getRecipientId(), "/queue/messages", savedMsg);
    }

    @MessageMapping("/typing")
    public void sendTypingIndicator(@Payload TypingIndicator typingIndicator) {
        messagingTemplate.convertAndSendToUser(typingIndicator.getRecipient(), "/queue/typing", typingIndicator);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<Message>> findChatMessages(@PathVariable String senderId, @PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.findMessages(senderId, recipientId));
    }
}