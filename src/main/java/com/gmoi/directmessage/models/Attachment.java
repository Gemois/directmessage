package com.gmoi.directmessage.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Attachment extends Auditable {
    @Id
    @GeneratedValue()
    private Long id;
    private String fileName;
    private String fileType;
    private long size;
    private String chatId;
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;
}