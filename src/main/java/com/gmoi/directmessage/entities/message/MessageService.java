package com.gmoi.directmessage.entities.message;

import com.gmoi.directmessage.entities.messageroom.MessageRoomService;
import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.mappers.MessageMapper;
import com.gmoi.directmessage.properties.MessageProperties;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository repository;
    private final MessageRoomService chatRoomService;
    private final MessageRepository messageRepository;
    private final MessageProperties messageProperties;

    public Message save(Message message) {
        log.info("Saving message from senderId: {} to recipientId: {}", message.getSenderId(), message.getRecipientId());

        var chatId = chatRoomService
                .getMessageRoomId(message.getSenderId(), message.getRecipientId(), true)
                .orElseThrow(() -> {
                    log.warn("Chat room not found for senderId: {} and recipientId: {}", message.getSenderId(), message.getRecipientId());
                    return new EntityNotFoundException("Chat room not found");
                });
        message.setChatId(chatId);
        repository.save(message);

        log.info("Message saved successfully with ID: {}", message.getId());
        return message;
    }

    public MessageDTO editMessage(Message editedMessage, User user) {
        log.info("Editing message with ID: {}", editedMessage.getId());

        Optional<Message> existingMessageOpt = messageRepository.findById(editedMessage.getId().toString());
        if (existingMessageOpt.isPresent()) {
            if (existingMessageOpt.get().getSenderId().equals(user.getId().toString())) {

            boolean canEdit = Duration.between(LocalDateTime.now(), existingMessageOpt.get().getCreatedAt()).toMinutes() < messageProperties.getEditMinutes();

            if (canEdit) {
                Message existingMessage = existingMessageOpt.get();
                existingMessage.setContent(editedMessage.getContent());
                existingMessage.setAttachment(editedMessage.getAttachment());
                existingMessage.setEdited(true);
                existingMessage.setEditedAt(LocalDateTime.now());
                Message editedMsg = messageRepository.save(existingMessage);
                log.info("Message edited successfully: {}", editedMsg);

                return MessageMapper.INSTANCE.toDto(editedMsg);
            } else {
                throw new IllegalStateException("User cannot edit this message.");
            }
        } else {
                throw new IllegalStateException("User cannot edit this message.");
            }
        }
        log.warn("Message with ID: {} not found for editing", editedMessage.getId());
        throw new EntityNotFoundException("Message not found");
    }

    public MessageDTO deleteMessage(Long messageId, User user) {
        log.info("Deleting message with ID: {}", messageId);

        Optional<Message> messageOpt = messageRepository.findById(messageId.toString());
        if (messageOpt.isPresent()) {
            if (messageOpt.get().getSenderId().equals(user.getId().toString())) {

                boolean canDelete = Duration.between(LocalDateTime.now(), messageOpt.get().getCreatedAt()).toMinutes() < messageProperties.getDeleteMinutes();

                if (canDelete) {
                    Message message = messageOpt.get();
                    message.setContent("");
                    message.setDeleted(true);
                    message.setDeletedAt(LocalDateTime.now());
                    Message deletedMsg = messageRepository.save(message);
                    log.info("Message deleted successfully: {}", deletedMsg);
                    return MessageMapper.INSTANCE.toDto(deletedMsg);
                } else {
                    log.warn("Delete window expired for message ID: {}", messageId);
                    throw new IllegalStateException("User cannot delete this message.");
                }
            } else {
                log.warn("Unauthorized delete attempt by user ID: {} for message ID: {}", user.getId(), messageId);
                throw new IllegalStateException("User cannot delete this message.");
            }
        }
        log.warn("Message with ID: {} not found for deletion", messageId);
        throw new EntityNotFoundException("Message not found");
    }

    public List<Message> findMessages(String senderId, String recipientId) {
        log.info("Finding messages between senderId: {} and recipientId: {}", senderId, recipientId);
        var chatId = chatRoomService.getMessageRoomId(senderId, recipientId, false);
        return chatId.map(repository::findByChatIdOrderByCreatedAtDesc).orElse(new ArrayList<>());
    }

    public ReadConfirmation markMessagesAsRead(ReadNotification readNotification) {
        log.info("Marking messages as read for chatId: {} up to date: {}", readNotification.getChatId(), readNotification.getReadUpToDate());

        String chatId = readNotification.getChatId();
        String recipientId = readNotification.getRecipientId();
        LocalDateTime readUpToDate = readNotification.getReadUpToDate();

        List<Message> unreadMessages = messageRepository.findUnreadMessagesBeforeDate(chatId, recipientId, readUpToDate);
        log.debug("Unread messages count: {}", unreadMessages.size());

        for (Message message : unreadMessages) {
            message.setRead(true);
            message.setReadAt(readUpToDate);
            messageRepository.save(message);
        }

        log.info("Marked {} messages as read for recipientId: {}", unreadMessages.size(), recipientId);
        return ReadConfirmation.builder()
                .chatId(chatId)
                .recipientId(recipientId)
                .readUpToDate(readUpToDate)
                .build();
    }

    public List<MessageDTO> searchMessages(String chatId, String query) {
        log.info("Searching messages in chatId: {} with query: {}", chatId, query);
        List<Message> messages = messageRepository.searchMessagesByKeyword(chatId, query);
        log.debug("Messages found: {}", messages.size());
        return MessageMapper.INSTANCE.toDto(messages);
    }

    public MessageDTO handlePinnedMessage(PinnedNotification messageToPin) {
        log.info("Handling pin/unpin for message ID: {}", messageToPin.getMessageId());
        Optional<Message> messageOptional = messageRepository.findById(messageToPin.getMessageId());
        if (messageOptional.isPresent()) {
            messageOptional.get().setPinned(messageToPin.isPinned());
            Message pinnedMessage = messageRepository.save(messageOptional.get());
            log.info("Message pin status updated: {}", pinnedMessage);
            return MessageMapper.INSTANCE.toDto(pinnedMessage);
        } else {
            log.warn("Message with ID: {} not found for pin/unpin", messageToPin.getMessageId());
            throw new EntityNotFoundException("Message not found");
        }
    }
}