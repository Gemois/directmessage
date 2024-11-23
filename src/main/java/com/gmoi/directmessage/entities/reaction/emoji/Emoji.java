package com.gmoi.directmessage.entities.reaction.emoji;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Emoji {
    @Id
    private String name;

    @Column(nullable = false)
    private String fileName;
}