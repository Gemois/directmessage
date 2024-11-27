package com.gmoi.directmessage.entities.messageroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRoomRepository extends JpaRepository<MessageRoom, String> {
    Optional<MessageRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
    Optional<MessageRoom> findByChatId(String chatId);
}
