package com.gmoi.directmessage.controllers;

import com.gmoi.directmessage.dtos.ReactionDTO;
import com.gmoi.directmessage.services.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reactions")
public class ReactionController {

    private final ReactionService reactionService;

    @GetMapping("/{messageId}")
    public ResponseEntity<List<ReactionDTO>> getReactions(@PathVariable String messageId) {
        List<ReactionDTO> reactions = reactionService.getReactions(messageId);
        return ResponseEntity.ok(reactions);
    }

    @PostMapping("/{messageId}")
    public ResponseEntity<Void> addReaction(@PathVariable String messageId, @RequestParam String emoji) {
        reactionService.addReaction(messageId, emoji);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> removeReaction(@PathVariable String messageId, @RequestParam String emoji) {
        reactionService.removeReaction(messageId, emoji);
        return ResponseEntity.noContent().build();
    }
}
