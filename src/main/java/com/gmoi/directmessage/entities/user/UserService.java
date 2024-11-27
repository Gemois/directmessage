package com.gmoi.directmessage.entities.user;

import com.gmoi.directmessage.entities.friendship.FriendshipService;
import com.gmoi.directmessage.mappers.UserMapper;
import com.gmoi.directmessage.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendshipService friendshipService;

    public UserDTO getUser() {
        log.info("Fetching current user details.");
        UserDTO userDto = UserMapper.INSTANCE.toDto(RequestUtil.getCurrentUser());
        log.info("Successfully fetched user details: {}", userDto);
        return userDto;
    }

    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users.");
        List<User> users = userRepository.findAll();
        log.info("Fetched {} users.", users.size());
        return UserMapper.INSTANCE.toDto(users);
    }

    public List<UserDTO> findConnectedUsers() {
        log.info("Fetching connected users (friends with status ONLINE).");
        List<UserDTO> connectedUsers = friendshipService.getFriends(RequestUtil.getCurrentUser())
                .stream()
                .filter(user -> user.getStatus().equals(UserStatus.ONLINE))
                .collect(Collectors.toList());
        log.info("Found {} connected users with status ONLINE.", connectedUsers.size());
        return connectedUsers;
    }

    public List<UserDTO> searchUsers(String query) {
        log.info("Searching for users with query: {}", query);
        List<User> users = userRepository.searchUsers(query);
        log.info("Found {} users matching the query.", users.size());
        updateLastActivity(RequestUtil.getCurrentUser());
        return UserMapper.INSTANCE.toDto(users);
    }

    public UserDTO updateUserDetails(UserDTO updatedUser) {
        log.info("Updating user details.");
        User existingUser = RequestUtil.getCurrentUser();

        if (updatedUser.getFirstName() != null) {
            log.debug("Updating first name to: {}", updatedUser.getFirstName());
            existingUser.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            log.debug("Updating last name to: {}", updatedUser.getLastName());
            existingUser.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getDisplayName() != null) {
            log.debug("Updating display name to: {}", updatedUser.getDisplayName());
            existingUser.setDisplayName(updatedUser.getDisplayName());
        }
        if (updatedUser.getEmail() != null) {
            log.debug("Updating email to: {}", updatedUser.getEmail());
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPhone() != null) {
            log.debug("Updating phone to: {}", updatedUser.getPhone());
            existingUser.setPhone(updatedUser.getPhone());
        }
        if (updatedUser.getStatus() != null) {
            log.debug("Updating status to: {}", updatedUser.getStatus());
            existingUser.setStatus(updatedUser.getStatus());
        }
        if (updatedUser.getPhoto() != null) {
            log.debug("Updating photo.");
            existingUser.setPhoto(updatedUser.getPhoto());
        }

        User savedUser = userRepository.save(existingUser);
        updateLastActivity(savedUser);

        log.info("Successfully updated user details.");
        return UserMapper.INSTANCE.toDto(savedUser);
    }

    public Void deleteUser() {
        log.info("Deleting current user.");
        User user = RequestUtil.getCurrentUser();
        userRepository.delete(user);
        log.info("User deleted successfully.");
        return null;
    }

    public User updateUserStatus(User user, UserStatus status) {
        log.info("Updating status of user with ID: {} to {}", user.getId(), status);

        if (status == UserStatus.ONLINE) {
            log.debug("Setting user status to ONLINE.");
            user.setStatus(UserStatus.ONLINE);
        } else if (status == UserStatus.OFFLINE) {
            log.debug("Setting user status to OFFLINE.");
            user.setStatus(UserStatus.OFFLINE);
        }

        User updatedUser = userRepository.save(user);
        updateLastActivity(user);
        log.info("User status updated to {}", updatedUser.getStatus());
        return updatedUser;
    }

    public void updateLastActivity(User user) {
        user.setLastActivityDate(LocalDateTime.now());
        userRepository.save(user);
    }

}
