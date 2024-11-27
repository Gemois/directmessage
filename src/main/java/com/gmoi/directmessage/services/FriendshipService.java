package com.gmoi.directmessage.services;

import com.gmoi.directmessage.dtos.FriendRequestDTO;
import com.gmoi.directmessage.dtos.UserDTO;
import com.gmoi.directmessage.mail.MailService;
import com.gmoi.directmessage.mappers.FriendRequestMapper;
import com.gmoi.directmessage.mappers.UserMapper;
import com.gmoi.directmessage.models.*;
import com.gmoi.directmessage.repositories.FriendRequestRepository;
import com.gmoi.directmessage.repositories.FriendshipRepository;
import com.gmoi.directmessage.repositories.UserRepository;
import com.gmoi.directmessage.utils.SessionUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final MailService mailService;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Transactional
    public void sendFriendRequest(Long recipientId) {
        log.info("Sending friend request to user with ID: {}", recipientId);

        User sender = SessionUtil.getCurrentUser();
        User recipient = userRepository.findById(recipientId).orElseThrow();

        if (friendshipRepository.existsByUser1AndUser2(sender, recipient) || friendshipRepository.existsByUser1AndUser2(recipient, sender)) {
            log.warn("Sender {} and recipient {} are already friends", sender.getEmail(), recipient.getEmail());
            throw new IllegalArgumentException("Already friends");
        }

        Optional<FriendRequest> existingRequest = friendRequestRepository.findBySenderAndRecipient(sender, recipient);
        if (existingRequest.isPresent()) {
            log.warn("Friend request from {} to {} already exists", sender.getEmail(), recipient.getEmail());
            throw new IllegalArgumentException("Friend request already sent");
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setRecipient(recipient);
        friendRequest.setStatus(FriendRequestStatus.PENDING);

        friendRequestRepository.save(friendRequest);
        log.info("Friend request saved successfully: {}", friendRequest);
        updateLastActivity(sender);

        mailService.sendFriendRequestEmail(friendRequest);
    }

    @Transactional
    public void acceptFriendRequest(Long requestId) {
        FriendRequest request = getFriendRequest(requestId);

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

        log.info("Friendships saved successfully between {} and {}", request.getSender().getEmail(), request.getRecipient().getEmail());

        request.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(request);
        updateLastActivity(request.getRecipient());

        log.info("Friend request updated to ACCEPTED: {}", request);
        mailService.sendFriendRequestAcceptedEmail(request);
    }

    @Transactional
    public void rejectFriendRequest(Long requestId) {
        FriendRequest request = getFriendRequest(requestId);

        request.setStatus(FriendRequestStatus.REJECTED);
        friendRequestRepository.save(request);
        updateLastActivity(request.getRecipient());
        log.info("Friend request updated to REJECTED: {}", request);
    }

    private FriendRequest getFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId).orElseThrow(() -> {
            log.warn("Friend request with ID {} not found", requestId);
            return new EntityNotFoundException("Request not found");
        });

        if (request.getStatus() != FriendRequestStatus.PENDING) {
            log.warn("Friend request with ID {} is not pending. Current status: {}", requestId, request.getStatus());
            throw new IllegalArgumentException("Request is not pending");
        }
        return request;
    }

    public List<UserDTO> getFriends(User user) {
        log.info("Fetching friends for user: {}", user.getEmail());
        List<Friendship> friendships = friendshipRepository.findByUser1OrUser2(user, user);
        List<User> friends = friendships.stream().map(friendship -> friendship.getUser1().equals(user) ? friendship.getUser2() : friendship.getUser1()).toList();
        log.debug("Friends fetched for user {}: {}", user.getEmail(), friends);
        return UserMapper.INSTANCE.toDto(friends);
    }

    public List<FriendRequestDTO> getPendingFriendRequests(User user) {
        log.info("Fetching pending friend requests for user: {}", user.getEmail());
        List<FriendRequest> friendRequests = friendRequestRepository.findByRecipientAndStatus(user, FriendRequestStatus.PENDING);
        log.debug("Pending friend requests fetched for user {}: {}", user.getEmail(), friendRequests);
        return FriendRequestMapper.INSTANCE.toDto(friendRequests);
    }


    private void updateLastActivity(User user) {
        user.setLastActivityDate(LocalDateTime.now());
        userRepository.save(user);
    }


}
