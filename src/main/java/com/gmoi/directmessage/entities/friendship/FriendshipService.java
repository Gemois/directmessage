package com.gmoi.directmessage.entities.friendship;

import com.gmoi.directmessage.entities.friendrequest.FriendRequest;
import com.gmoi.directmessage.entities.friendrequest.FriendRequestDTO;
import com.gmoi.directmessage.entities.friendrequest.FriendRequestRepository;
import com.gmoi.directmessage.entities.friendrequest.FriendRequestStatus;
import com.gmoi.directmessage.entities.user.User;
import com.gmoi.directmessage.entities.user.UserDTO;
import com.gmoi.directmessage.entities.user.UserRepository;
import com.gmoi.directmessage.mail.MailService;
import com.gmoi.directmessage.mappers.FriendRequestMapper;
import com.gmoi.directmessage.mappers.UserMapper;
import com.gmoi.directmessage.utils.RequestUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final MailService mailService;

    @Transactional
    public void sendFriendRequest(Long recipientId) {

        User sender = RequestUtil.getCurrentUser();
        User recipient = userRepository.findById(recipientId).orElseThrow();

        if (friendshipRepository.existsByUser1AndUser2(sender, recipient) || friendshipRepository.existsByUser1AndUser2(recipient, sender)) {
            throw new IllegalArgumentException("Already friends");
        }

        Optional<FriendRequest> existingRequest = friendRequestRepository.findBySenderAndRecipient(sender, recipient);
        if (existingRequest.isPresent()) {
            throw new IllegalArgumentException("Friend request already sent");
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setRecipient(recipient);
        friendRequest.setStatus(FriendRequestStatus.PENDING);

        friendRequestRepository.save(friendRequest);
        mailService.sendFriendRequestEmail(friendRequest);
    }

    @Transactional
    public void acceptFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId).orElseThrow();
        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new IllegalArgumentException("Request is not pending");
        }

        Friendship friendship1 = new Friendship();
        friendship1.setUser1(request.getSender());
        friendship1.setUser2(request.getRecipient());
        friendship1.setStatus(FriendshipStatus.ACTIVE);
        friendshipRepository.save(friendship1);

        Friendship friendship2 = new Friendship();
        friendship2.setUser1(request.getRecipient());
        friendship2.setUser2(request.getSender());
        friendship2.setStatus(FriendshipStatus.ACTIVE);
        friendshipRepository.save(friendship2);

        request.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(request);
        mailService.sendFriendRequestAcceptedEmail(request);
    }

    @Transactional
    public void rejectFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId).orElseThrow();
        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new IllegalArgumentException("Request is not pending");
        }

        request.setStatus(FriendRequestStatus.REJECTED);
        friendRequestRepository.save(request);
    }

    public List<UserDTO> getFriends(User user) {
        List<Friendship> friendships = friendshipRepository.findByUser1OrUser2(user, user);
        List<User> friends = friendships.stream().map(friendship -> friendship.getUser1().equals(user) ? friendship.getUser2() : friendship.getUser1()).toList();
        return UserMapper.INSTANCE.toDto(friends);
    }

    public List<FriendRequestDTO> getPendingFriendRequests(User user) {
        List<FriendRequest> friendRequests = friendRequestRepository.findByRecipientAndStatus(user, FriendRequestStatus.PENDING);
        return FriendRequestMapper.INSTANCE.toDto(friendRequests);
    }

}
