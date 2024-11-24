package com.gmoi.directmessage.entities.reaction;

import com.gmoi.directmessage.entities.message.Message;
import com.gmoi.directmessage.entities.message.MessageRepository;
import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.entities.user.UserService;
import com.gmoi.directmessage.utils.RequestUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionService {

    private final UserService userService;
    private final MessageRepository messageRepository;
    private final ReactionRepository reactionRepository;

    public List<ReactionDTO> getReactions(String messageId) {
        log.info("Fetching reactions for messageId: {}", messageId);

        Message message = getMessage(messageId);
        log.debug("Message found for messageId: {}", messageId);

        List<Reaction> reactions = reactionRepository.findReactionByMessage(message);
        log.info("Found {} reactions for messageId: {}", reactions.size(), messageId);

        Map<String, Long> emojiCounts = reactions.stream()
                .collect(Collectors.groupingBy(Reaction::getEmoji, Collectors.counting()));

        log.debug("Aggregated emoji counts for messageId: {}", messageId);

        return emojiCounts.entrySet().stream()
                .map(entry -> ReactionDTO
                        .builder()
                        .emoji(entry.getKey())
                        .count(entry.getValue().intValue())
                        .url("/api/v1/emojis/" + entry.getKey()).build())
                .collect(Collectors.toList());
    }

    public void addReaction(String messageId, String emoji) {
        log.info("Adding reaction '{}' to messageId: {}", emoji, messageId);

        User user = RequestUtil.getCurrentUser();
        log.debug("Current user: {}", user.getId());

        Message message = getMessage(messageId);
        if (reactionRepository.existsByMessageAndUserAndEmoji(message, user, emoji)) {
            log.warn("Reaction already exists for messageId: {}, userId: {}, emoji: {}", messageId, user.getId(), emoji);
            throw new IllegalStateException("Reaction already exists");
        }

        Reaction reaction = new Reaction();
        reaction.setMessage(message);
        reaction.setUser(user);
        reaction.setEmoji(emoji);
        reactionRepository.save(reaction);
        userService.updateLastActivity(user);

        log.info("Reaction '{}' added to messageId: {} by userId: {}", emoji, messageId, user.getId());
    }

    public void removeReaction(String messageId, String emoji) {
        log.info("Removing reaction '{}' from messageId: {}", emoji, messageId);

        User user = RequestUtil.getCurrentUser();
        Message message = getMessage(messageId);
        Reaction reaction = reactionRepository.findByMessageAndUserAndEmoji(message, user, emoji)
                .orElseThrow(() -> {
                    log.error("Reaction not found for messageId: {}, userId: {}, emoji: {}", messageId, user.getId(), emoji);
                    return new EntityNotFoundException("Reaction not found");
                });

        reactionRepository.delete(reaction);
        userService.updateLastActivity(user);
        log.info("Reaction '{}' removed from messageId: {} by userId: {}", emoji, messageId, user.getId());
    }

    private Message getMessage(String messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> {
                    log.error("Message not found for messageId: {}", messageId);
                    return new EntityNotFoundException("Message not found");
                });
    }
}
