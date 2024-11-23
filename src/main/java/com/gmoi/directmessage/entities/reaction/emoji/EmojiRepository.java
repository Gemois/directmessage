package com.gmoi.directmessage.entities.reaction.emoji;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmojiRepository extends JpaRepository<Emoji, String> {
}
