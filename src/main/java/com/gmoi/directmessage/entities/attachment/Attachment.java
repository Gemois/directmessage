package com.gmoi.directmessage.entities.attachment;

import com.gmoi.directmessage.entities.Auditable;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Attachment extends Auditable {
    @Id
    @GeneratedValue()
    private Long id;
    private String fileName;
    private String fileType;
    private long size;
}