package com.gmoi.directmessage.entities.attachment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue()
    private Long id;
    private String fileName;
    private String fileType;
    private long size;
}