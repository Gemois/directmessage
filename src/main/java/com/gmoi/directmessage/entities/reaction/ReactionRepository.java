package com.gmoi.directmessage.entities.reaction;

import com.gmoi.directmessage.entities.message.Message;
import com.gmoi.directmessage.entities.user.User;
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
