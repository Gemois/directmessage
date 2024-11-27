package com.gmoi.directmessage.controllers;

import com.gmoi.directmessage.dtos.FriendRequestDTO;
import com.gmoi.directmessage.dtos.UserDTO;
import com.gmoi.directmessage.services.FriendshipService;
import com.gmoi.directmessage.utils.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getFriends() {
        List<UserDTO> friends = friendshipService.getFriends(SessionUtil.getCurrentUser());
        return ResponseEntity.ok(friends);
    }

    @PostMapping("/friend-request/{recipientId}")
    public ResponseEntity<Void> sendFriendRequest(@PathVariable Long recipientId) {
        friendshipService.sendFriendRequest(recipientId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friend-request/{requestId}/accept")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long requestId) {
        friendshipService.acceptFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friend-request/{requestId}/reject")
    public ResponseEntity<Void> rejectFriendRequest(@PathVariable Long requestId) {
        friendshipService.rejectFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<List<FriendRequestDTO>> getPendingRequests() {
        List<FriendRequestDTO> pendingRequests = friendshipService.getPendingFriendRequests(SessionUtil.getCurrentUser());
        return ResponseEntity.ok(pendingRequests);
    }
}
