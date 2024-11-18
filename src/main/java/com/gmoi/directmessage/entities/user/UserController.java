package com.gmoi.directmessage.entities.user;

import com.gmoi.directmessage.entities.friendship.FriendshipService;
import com.gmoi.directmessage.entities.message.MessageDestination;
import com.gmoi.directmessage.utils.WebSocketUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController()
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FriendshipService friendshipService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping()
    public ResponseEntity<UserDTO> getUser() {
        return ResponseEntity.ok(userService.getUser());
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser() {
        return ResponseEntity.ok(userService.deleteUser());
    }

    @PatchMapping()
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO updatedUser) {
        return ResponseEntity.ok(userService.updateUserDetails(updatedUser));
    }

    @MessageMapping("user/status")
    public void updateUserStatus(@AuthenticationPrincipal Principal principal, @Payload UserStatus status) {
        User updatedUser = userService.updateUserStatus(WebSocketUtil.getCurrentUser(principal), status);

        for (UserDTO friend : friendshipService.getFriends(updatedUser)) {
            messagingTemplate.convertAndSendToUser(friend.getId().toString(), MessageDestination.STATUS.getDestination(), updatedUser);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/connected")
    public ResponseEntity<List<UserDTO>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam() String query) {
        List<UserDTO> result = userService.searchUsers(query);
        return ResponseEntity.ok(result);
    }
}
