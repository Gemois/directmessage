package com.gmoi.directmessage.user;

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
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        return ResponseEntity.ok(userService.getCurrentUser(jwt));
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
