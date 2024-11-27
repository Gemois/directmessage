package com.gmoi.directmessage.controllers;

import com.gmoi.directmessage.dtos.FriendRequestDTO;
import com.gmoi.directmessage.dtos.UserDTO;
import com.gmoi.directmessage.services.FriendshipService;
import com.gmoi.directmessage.utils.SessionUtil;
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
        return friendshipService.getFriends(SessionUtil.getCurrentUser());
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
        return friendshipService.getPendingFriendRequests(SessionUtil.getCurrentUser());
    }

}
