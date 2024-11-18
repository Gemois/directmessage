package com.gmoi.directmessage.entities.message;

import com.gmoi.directmessage.entities.messageroom.MessageRoomService;
import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.mappers.MessageMapper;
import com.gmoi.directmessage.properties.MessageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository repository;
    private final MessageRoomService chatRoomService;
    private final MessageRepository messageRepository;
    private final MessageProperties messageProperties;

    public Message save(Message message) {
        var chatId = chatRoomService
                .getMessageRoomId(message.getSenderId(), message.getRecipientId(), true)
                .orElseThrow();
        message.setChatId(chatId);
        message.setCreatedAt(LocalDateTime.now());
        repository.save(message);
        return message;
    }

    public MessageDTO editMessage(Message editedMessage, User user) {
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
                return MessageMapper.INSTANCE.toDto(editedMsg);
            } else {
                throw new RuntimeException("User cannot edit this message.");
            }
        } else {
                throw new RuntimeException("User cannot edit this message.");
            }
        }
        throw new RuntimeException("Message not found");
    }

    public MessageDTO deleteMessage(Long messageId, User user) {

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
                    return MessageMapper.INSTANCE.toDto(deletedMsg);
                } else {
                    throw new RuntimeException("User cannot delete this message.");
                }
            } else {
                throw new RuntimeException("User cannot delete this message.");
            }
        }
        throw new RuntimeException("Message not found");
    }

    public List<Message> findMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getMessageRoomId(senderId, recipientId, false);
        return chatId.map(repository::findByChatIdOrderByCreatedAtDesc).orElse(new ArrayList<>());
    }

    public ReadConfirmation markMessagesAsRead(ReadNotification readNotification) {
        String chatId = readNotification.getChatId();
        String recipientId = readNotification.getRecipientId();
        LocalDateTime readUpToDate = readNotification.getReadUpToDate();

        List<Message> unreadMessages = messageRepository.findUnreadMessagesBeforeDate(chatId, recipientId, readUpToDate);
        for (Message message : unreadMessages) {
            message.setRead(true);
            message.setReadAt(readUpToDate);
            messageRepository.save(message);
        }

        return ReadConfirmation.builder()
                .chatId(chatId)
                .recipientId(recipientId)
                .readUpToDate(readUpToDate)
                .build();
    }

    public List<MessageDTO> searchMessages(String chatId, String query) {
        List<Message> messages =  messageRepository.searchMessagesByKeyword(chatId, query);
        return MessageMapper.INSTANCE.toDto(messages);
    }

    public MessageDTO handlePinnedMessage(PinnedNotification messageToPin) {
        Optional<Message> messageOptional = messageRepository.findById(messageToPin.getMessageId());
        if (messageOptional.isPresent()) {
            messageOptional.get().setPinned(messageToPin.isPinned());
            Message pinnedMessage = messageRepository.save(messageOptional.get());
            return MessageMapper.INSTANCE.toDto(pinnedMessage);
        } else {
            throw new RuntimeException("Message not found");
        }
    }





}