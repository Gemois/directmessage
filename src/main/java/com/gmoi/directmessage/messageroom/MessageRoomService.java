package com.gmoi.directmessage.messageroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageRoomService {

    private final MessageRoomRepository messageRoomRepository;

    public Optional<String> getMessageRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists) {
        return messageRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(MessageRoom::getChatId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        var chatId = createMessageRoomId(senderId, recipientId);
                        return Optional.of(chatId);
                    }

                    return Optional.empty();
                });
    }

    private String createMessageRoomId(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        MessageRoom senderRecipient = MessageRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        MessageRoom recipientSender = MessageRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        messageRoomRepository.save(senderRecipient);
        messageRoomRepository.save(recipientSender);

        return chatId;
    }
}