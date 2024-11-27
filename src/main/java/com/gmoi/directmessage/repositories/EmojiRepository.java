package com.gmoi.directmessage.repositories;

import com.gmoi.directmessage.models.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmojiRepository extends JpaRepository<Emoji, String> {
}
