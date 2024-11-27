package com.gmoi.directmessage.dtos;

import com.gmoi.directmessage.models.FriendRequestStatus;
import com.gmoi.directmessage.models.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FriendRequestDTO {

    private Long requestId;
    private User sender;
    private User recipient;
    private FriendRequestStatus status;
    private LocalDateTime createdAt;

}
