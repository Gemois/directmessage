package com.gmoi.directmessage.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AttachmentDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private long size;
    private LocalDateTime createdAt;
}
