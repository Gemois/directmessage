package com.gmoi.directmessage.entities.reaction;

import com.gmoi.directmessage.entities.message.Message;
import com.gmoi.directmessage.entities.message.MessageRepository;
import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.utils.RequestUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final MessageRepository messageRepository;

    public List<ReactionDTO> getReactions(String messageId) {

        Message message = messageRepository.findById(messageId).orElseThrow(EntityNotFoundException::new);
        List<Reaction> reactions = reactionRepository.findReactionByMessage(message);

        Map<String, Long> emojiCounts = reactions.stream()
                .collect(Collectors.groupingBy(Reaction::getEmoji, Collectors.counting()));

        return emojiCounts.entrySet().stream()
                .map(entry -> ReactionDTO
                        .builder()
                        .emoji(entry.getKey())
                        .count(entry.getValue().intValue())
                        .url("/api/v1/emojis/" + entry.getKey()).build())
                .collect(Collectors.toList());
    }

    public void addReaction(String messageId, String emoji) {
        User user = RequestUtil.getCurrentUser();
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found"));

        if (reactionRepository.existsByMessageAndUserAndEmoji(message, user, emoji)) {
            throw new IllegalStateException("Reaction already exists");
        }

        Reaction reaction = new Reaction();
        reaction.setMessage(message);
        reaction.setUser(user);
        reaction.setEmoji(emoji);
        reactionRepository.save(reaction);
    }

    public void removeReaction(String messageId, String emoji) {
        User user = RequestUtil.getCurrentUser();
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found"));

        Reaction reaction = reactionRepository.findByMessageAndUserAndEmoji(message, user, emoji)
                .orElseThrow(() -> new EntityNotFoundException("Reaction not found"));

        reactionRepository.delete(reaction);
    }
}
