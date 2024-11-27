package com.gmoi.directmessage.initializers;

import com.gmoi.directmessage.models.Emoji;
import com.gmoi.directmessage.repositories.EmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmojiInitializer implements ApplicationRunner {
    private final EmojiRepository emojiRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (emojiRepository.count() == 0) {
            List<Emoji> initialEmojis = List.of(
                    new Emoji("winking", "winking_face.png"),
                    new Emoji("sleeping", "sleeping_face.png"),
                    new Emoji("praying", "praying_hands.png"),
                    new Emoji("grinning", "grinning_face.png"),
                    new Emoji("exploding", "exploding_head.png")
            );

            emojiRepository.saveAll(initialEmojis);
            System.out.println("Emoji table initialized with default data.");
        }
    }
}
