package com.gmoi.directmessage.repositories;

import com.gmoi.directmessage.models.Message;
import com.gmoi.directmessage.models.Reaction;
import com.gmoi.directmessage.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    boolean existsByMessageAndUserAndEmoji(Message message, User user, String emoji);
    Optional<Reaction> findByMessageAndUserAndEmoji(Message message, User user, String emoji);

    List<Reaction> findReactionByMessage(Message message);
}
