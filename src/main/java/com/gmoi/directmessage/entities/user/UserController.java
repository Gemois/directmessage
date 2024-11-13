package com.gmoi.directmessage.entities.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/connected")
    public ResponseEntity<List<UserDto>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam() String query) {
        List<UserDto> result = userService.searchUsers(query);
        return ResponseEntity.ok(result);
    }

    @MessageMapping("user/connect")
    @SendTo("/user/public")
    @Payload(required = false)
    public User addUser(@Header(HttpHeaders.AUTHORIZATION) String jwt) {
        return userService.connect(jwt);
    }

    @MessageMapping("user/disconnect")
    @SendTo("/user/public")
    @Payload(required = false)
    public User disconnectUser(@Header(HttpHeaders.AUTHORIZATION) String jwt) {
        return userService.disconnect(jwt);
    }
}
