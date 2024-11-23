package com.gmoi.directmessage.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.*;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class Auditable {

    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Long lastModifiedBy;

    @Version
    private long version;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modifiedDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
