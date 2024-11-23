package com.gmoi.directmessage.entities.friendrequest;

import com.gmoi.directmessage.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findBySenderAndRecipient(User sender, User recipient);

    List<FriendRequest> findByRecipientAndStatus(User recipient, FriendRequestStatus status);

}
