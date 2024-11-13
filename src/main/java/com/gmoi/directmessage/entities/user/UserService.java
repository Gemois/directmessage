package com.gmoi.directmessage.entities.user;

import com.gmoi.directmessage.auth.JwtService;
import com.gmoi.directmessage.entities.friendship.FriendshipService;
import com.gmoi.directmessage.mappers.UserMapper;
import com.gmoi.directmessage.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendshipService friendshipService;
    private final JwtService jwtService;

    public UserDto getCurrentUser() {
        return UserMapper.INSTANCE.userToUserDto(RequestUtil.getCurrentUser());
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.INSTANCE.usersToUserDtos(users);
    }

    public List<UserDto> findConnectedUsers() {
        List<User> onlineFriends = friendshipService.getFriends(RequestUtil.getCurrentUser())
                .stream()
                .filter(user -> user.getStatus().equals(UserStatus.ONLINE))
                .collect(Collectors.toList());

        return UserMapper.INSTANCE.usersToUserDtos(onlineFriends);
    }

    public User connect(String jwt) {
        User user = findUser(jwt);
        user.setStatus(UserStatus.ONLINE);
        userRepository.save(user);
        return user;
    }

    public User disconnect(String jwt) {
        User user = findUser(jwt);
        user.setStatus(UserStatus.OFFLINE);
        userRepository.save(user);
        return user;
    }

    public List<UserDto> searchUsers(String searchTerm) {
        List<User> users = userRepository.searchUsers(searchTerm);
        return UserMapper.INSTANCE.usersToUserDtos(users);
    }

    private User findUser(String jwt) {
        String email = jwtService.extractUsername(jwt.substring(7));
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return user.get();
    }
}
