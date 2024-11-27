package com.gmoi.directmessage.repositories;

import com.gmoi.directmessage.models.FriendRequestStatus;
import com.gmoi.directmessage.models.User;
import com.gmoi.directmessage.models.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findBySenderAndRecipient(User sender, User recipient);

    List<FriendRequest> findByRecipientAndStatus(User recipient, FriendRequestStatus status);

}
