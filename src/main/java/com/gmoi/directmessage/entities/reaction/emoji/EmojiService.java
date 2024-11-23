package com.gmoi.directmessage.entities.reaction.emoji;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmojiService {

    private final EmojiRepository emojiRepository;

    public List<Emoji> getAllEmojis(String baseUrl) {
        return emojiRepository.findAll().stream()
                .map(emoji -> new Emoji(
                        emoji.getName(),
                        baseUrl + "/api/v1/emojis/" + emoji.getName()
                ))
                .collect(Collectors.toList());
    }

    public Resource getEmojiResource(String emojiName) {
        try {

            Optional<Emoji> emoji = emojiRepository.findById(emojiName);
            if (emoji.isEmpty()) {
                throw new FileNotFoundException(emojiName);
            }

            Resource imageResource = new ClassPathResource("emojis/" + emoji.get().getFileName());
            if (imageResource.exists() && imageResource.isReadable()) {
                return imageResource;
            } else {
                throw new FileNotFoundException("Emoji image not found or unreadable: " + emojiName);
            }
        } catch (IOException e) {
            throw new EntityNotFoundException("Error retrieving the emoji image", e);
        }
    }
}
