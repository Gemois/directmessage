package com.gmoi.directmessage.entities.messageroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageRoomService {

    private final MessageRoomRepository messageRoomRepository;

    public Optional<String> getMessageRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists) {
        log.info("Fetching message room ID for senderId: {} and recipientId: {}", senderId, recipientId);

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
        log.info("Generating new chatId for senderId: {} and recipientId: {}", senderId, recipientId);
        String chatId = String.format("%s_%s", senderId, recipientId);

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

        log.info("Message rooms successfully created with chatId: {}", chatId);
        return chatId;
    }
}