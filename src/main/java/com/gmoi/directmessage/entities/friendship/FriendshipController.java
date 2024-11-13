package com.gmoi.directmessage.entities.friendship;


import com.gmoi.directmessage.entities.friendrequest.FriendRequest;
import com.gmoi.directmessage.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/friend-request/{recipientId}")
    public void sendFriendRequest(@PathVariable Long recipientId) {
        friendshipService.sendFriendRequest(recipientId);
    }

    @PostMapping("/friend-request/{requestId}/accept")
    public void acceptFriendRequest(@PathVariable Long requestId) {
        friendshipService.acceptFriendRequest(requestId);
    }

    @PostMapping("/friend-request/{requestId}/reject")
    public void rejectFriendRequest(@PathVariable Long requestId) {
        friendshipService.rejectFriendRequest(requestId);
    }

    @GetMapping("/{userId}")
    public List<User> getFriends(@PathVariable Long userId) {
        return friendshipService.getFriends(userId);
    }

    @GetMapping("/{userId}/pending-requests")
    public List<FriendRequest> getPendingRequests(@PathVariable Long userId) {
        return friendshipService.getPendingFriendRequests(userId);
    }

}
