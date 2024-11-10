package com.gmoi.directmessage.messageroom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, String> {
    Optional<MessageRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
