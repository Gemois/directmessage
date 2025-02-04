package com.gmoi.directmessage.controllers;

import com.gmoi.directmessage.models.Emoji;
import com.gmoi.directmessage.services.EmojiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/emojis")
public class EmojiController {

    private final EmojiService emojiService;

    @GetMapping("/{emojiName}")
    public ResponseEntity<Resource> getEmoji(@PathVariable String emojiName) {
        try {
            Resource imageResource = emojiService.getEmojiResource(emojiName);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageResource);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping()
    public ResponseEntity<List<Emoji>> getEmojis(HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        List<Emoji> emojis = emojiService.getAllEmojis(baseUrl);
        return ResponseEntity.ok(emojis);
    }
}