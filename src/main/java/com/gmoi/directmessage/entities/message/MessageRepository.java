package com.gmoi.directmessage.entities.message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByChatIdOrderByTimestampAsc(String chatId);
}