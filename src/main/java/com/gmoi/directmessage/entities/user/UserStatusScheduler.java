package com.gmoi.directmessage.entities.user;

import com.gmoi.directmessage.entities.friendship.FriendshipService;
import com.gmoi.directmessage.entities.message.MessageDestination;
import com.gmoi.directmessage.properties.UserProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStatusScheduler {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final FriendshipService friendshipService;
    private final UserProperties userProperties;

    @Scheduled(fixedRate = 60000)
    public void checkForIdleUsers() {
        log.info("Starting check for idle users.");
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(userProperties.getStatus().getInactiveThresholdMinutes());
        List<User> activeUsers = userRepository.findByStatus(UserStatus.ONLINE);

        for (User user : activeUsers) {
            if (user.getLastActivityDate().isBefore(threshold)) {
                log.info("User with ID {} is idle, changing status to INACTIVE.", user.getId());
                user.setStatus(UserStatus.INACTIVE);
                userRepository.save(user);
                log.info("User with ID {} status updated to INACTIVE.", user.getId());
                notifyStatusChange(user);
            }
        }

        log.info("Completed check for idle users.");
    }

    private void notifyStatusChange(User user) {
        log.info("Notifying friends about status change of user with ID {}.", user.getId());
        for (UserDTO friend : friendshipService.getFriends(user)) {
            log.debug("Sending status change notification to friend with ID {}.", friend.getId());
            messagingTemplate.convertAndSendToUser(friend.getId().toString(), MessageDestination.STATUS.getDestination(), user);
        }
        log.info("Status change notifications sent for user with ID {}.", user.getId());
    }
}