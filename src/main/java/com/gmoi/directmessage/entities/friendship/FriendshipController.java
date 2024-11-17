package com.gmoi.directmessage.entities.friendship;

import com.gmoi.directmessage.entities.friendrequest.FriendRequestDTO;
import com.gmoi.directmessage.entities.user.UserDTO;
import com.gmoi.directmessage.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @GetMapping()
    public List<UserDTO> getFriends() {
        return friendshipService.getFriends(RequestUtil.getCurrentUser());
    }

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

    @GetMapping("/pending-requests")
    public List<FriendRequestDTO> getPendingRequests() {
        return friendshipService.getPendingFriendRequests(RequestUtil.getCurrentUser());
    }

}
