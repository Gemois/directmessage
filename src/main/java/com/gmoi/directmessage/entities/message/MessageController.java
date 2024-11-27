package com.gmoi.directmessage.entities.message;

import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.utils.WebSocketUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send-message")
    public void sendMessage(@Payload Message message) {
        Message savedMsg = messageService.save(message);
        messagingTemplate.convertAndSendToUser(message.getRecipientId(), MessageDestination.MESSAGES.getDestination(), savedMsg);
    }

    @MessageMapping("/edit-message")
    public void editMessage(@Payload Message editedMessage, @AuthenticationPrincipal Principal principal) {
        User user = WebSocketUtil.getCurrentUser(principal);
        MessageDTO updatedMessage = messageService.editMessage(editedMessage, user);
        messagingTemplate.convertAndSendToUser(editedMessage.getRecipientId(), MessageDestination.MESSAGES.getDestination(), updatedMessage);
    }

    @MessageMapping("/delete-message")
    public void deleteMessage(@Payload Long messageId, @AuthenticationPrincipal Principal principal) {
        User user = WebSocketUtil.getCurrentUser(principal);
        MessageDTO deletedMsg = messageService.deleteMessage(messageId, user);
        messagingTemplate.convertAndSendToUser(user.getId().toString(), MessageDestination.MESSAGES.getDestination(), deletedMsg);
    }

    @MessageMapping("/typing")
    public void sendTypingIndicator(@Payload TypingIndicator typingIndicator) {
        messagingTemplate.convertAndSendToUser(typingIndicator.getRecipient(), MessageDestination.TYPING.getDestination(), typingIndicator);
    }

    @MessageMapping("/mark-read")
    public void markMessagesAsRead(@Payload ReadNotification readNotification) {
        ReadConfirmation confirmation = messageService.markMessagesAsRead(readNotification);
        messagingTemplate.convertAndSendToUser(readNotification.getSenderId(), MessageDestination.READ.getDestination(), confirmation);
    }

    @MessageMapping("/pin-message")
    public void handlePinnedMessage(@Payload PinnedNotification pinnedNotification) {
        MessageDTO messageDTO = messageService.handlePinnedMessage(pinnedNotification);
        messagingTemplate.convertAndSendToUser(messageDTO.getRecipientId(), MessageDestination.MESSAGES.getDestination(), messageDTO);
        messagingTemplate.convertAndSendToUser(messageDTO.getSenderId(), MessageDestination.MESSAGES.getDestination(), messageDTO);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<Message>> findChatMessages(@PathVariable String senderId, @PathVariable String recipientId) {
        return ResponseEntity.ok(messageService.findMessages(senderId, recipientId));
    }

    @GetMapping("/messages/search")
    public ResponseEntity<List<MessageDTO>> searchMessages(@RequestParam String chatId, @RequestParam String query) {
        List<MessageDTO> messages = messageService.searchMessages(chatId, query);
        return ResponseEntity.ok(messages);
    }
}