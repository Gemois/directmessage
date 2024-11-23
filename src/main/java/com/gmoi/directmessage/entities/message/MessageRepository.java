package com.gmoi.directmessage.entities.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByChatIdOrderByCreatedAtDesc(String chatId);

    @Query("SELECT m FROM Message m WHERE m.chatId = :chatId AND m.recipientId = :recipientId AND m.read = false AND m.createdAt <= :readUpToDate")
    List<Message> findUnreadMessagesBeforeDate(String chatId, String recipientId, LocalDateTime readUpToDate);

    @Query("SELECT m FROM Message m WHERE (m.chatId = :chatId) AND (m.content LIKE %:query%) AND m.isDeleted = false")
    List<Message> searchMessagesByKeyword(@Param("chatId") String chatId, @Param("query") String query);

}