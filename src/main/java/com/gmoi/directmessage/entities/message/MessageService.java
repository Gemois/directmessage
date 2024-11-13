package com.gmoi.directmessage.entities.message;

import com.gmoi.directmessage.entities.messageroom.MessageRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository repository;
    private final MessageRoomService chatRoomService;

    public Message save(Message message) {
        var chatId = chatRoomService
                .getMessageRoomId(message.getSenderId(), message.getRecipientId(), true)
                .orElseThrow();
        message.setChatId(chatId);
        message.setTimestamp(new Date());
        repository.save(message);
        return message;
    }

    public List<Message> findMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getMessageRoomId(senderId, recipientId, false);
        return chatId.map(repository::findByChatIdOrderByTimestampAsc).orElse(new ArrayList<>());
    }
}