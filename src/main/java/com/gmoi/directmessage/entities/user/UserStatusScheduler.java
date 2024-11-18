package com.gmoi.directmessage.entities.user;

import com.gmoi.directmessage.entities.friendship.FriendshipService;
import com.gmoi.directmessage.entities.message.MessageDestination;
import com.gmoi.directmessage.properties.UserProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserStatusScheduler {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final FriendshipService friendshipService;
    private final UserProperties userProperties;

    @Scheduled(fixedRate = 60000)
    public void checkForIdleUsers() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(userProperties.getStatus().getInactiveThresholdMinutes());
        List<User> activeUsers = userRepository.findByStatus(UserStatus.ONLINE);

        for (User user : activeUsers) {
            if (user.getLastActivityDate().isBefore(threshold)) {
                user.setStatus(UserStatus.INACTIVE);
                userRepository.save(user);
                notifyStatusChange(user);
            }
        }
    }

    private void notifyStatusChange(User user) {
        for (UserDTO friend : friendshipService.getFriends(user)) {
            messagingTemplate.convertAndSendToUser(friend.getId().toString(), MessageDestination.STATUS.getDestination(), user);
        }
    }
}