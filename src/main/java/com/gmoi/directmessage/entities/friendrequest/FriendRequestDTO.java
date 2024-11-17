package com.gmoi.directmessage.entities.friendrequest;

import com.gmoi.directmessage.entities.user.User;
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
