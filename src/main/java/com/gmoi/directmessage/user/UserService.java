package com.gmoi.directmessage.user;

import com.gmoi.directmessage.auth.JwtService;
import com.gmoi.directmessage.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserDto getCurrentUser(String jwt) {
        return UserMapper.INSTANCE.userToUserDto(findUser(jwt));
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.INSTANCE.usersToUserDtos(users);
    }

    public List<User> findConnectedUsers() {
        return userRepository.findAllByStatus(Status.ONLINE);
    }

    public User connect(String jwt) {
        User user = findUser(jwt);
        user.setStatus(Status.ONLINE);
        userRepository.save(user);
        return user;
    }

    public User disconnect(String jwt) {
        User user = findUser(jwt);
        user.setStatus(Status.OFFLINE);
        userRepository.save(user);
        return user;
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
