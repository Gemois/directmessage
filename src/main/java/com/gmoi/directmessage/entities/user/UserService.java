package com.gmoi.directmessage.entities.user;

import com.gmoi.directmessage.entities.friendship.FriendshipService;
import com.gmoi.directmessage.mappers.UserMapper;
import com.gmoi.directmessage.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendshipService friendshipService;

    public UserDTO getUser() {
        return UserMapper.INSTANCE.toDto(RequestUtil.getCurrentUser());
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.INSTANCE.toDto(users);
    }

    public List<UserDTO> findConnectedUsers() {
        return friendshipService.getFriends(RequestUtil.getCurrentUser())
                .stream()
                .filter(user -> user.getStatus().equals(UserStatus.ONLINE))
                .collect(Collectors.toList());
    }

    public List<UserDTO> searchUsers(String query) {
        List<User> users = userRepository.searchUsers(query);
        return UserMapper.INSTANCE.toDto(users);
    }

    public UserDTO updateUserDetails(UserDTO updatedUser) {
        User existingUser = RequestUtil.getCurrentUser();

        if (updatedUser.getFirstName() != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getDisplayName() != null) {
            existingUser.setDisplayName(updatedUser.getDisplayName());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPhone() != null) {
            existingUser.setPhone(updatedUser.getPhone());
        }
        if (updatedUser.getStatus() != null) {
            existingUser.setStatus(updatedUser.getStatus());
        }
        if (updatedUser.getPhoto() != null) {
            existingUser.setPhoto(updatedUser.getPhoto());
        }

        userRepository.save(existingUser);
        return UserMapper.INSTANCE.toDto(existingUser);
    }

    public Void deleteUser() {
        User user = RequestUtil.getCurrentUser();
        userRepository.delete(user);
        return null;
    }

    public User updateUserStatus(User user, UserStatus status) {

        if (status == UserStatus.ONLINE) {
           user.setStatus(UserStatus.ONLINE);
        } else if (status == UserStatus.OFFLINE) {
            user.setStatus(UserStatus.OFFLINE);
        }

        return userRepository.save(user);
    }
}
