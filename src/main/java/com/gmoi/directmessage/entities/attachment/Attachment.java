package com.gmoi.directmessage.entities.attachment;

import com.gmoi.directmessage.entities.Auditable;
import com.gmoi.directmessage.entities.user.User;
import jakarta.persistence.*;
import jakarta.persistence.Id;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;
}