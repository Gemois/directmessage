package com.gmoi.directmessage.entities.friendship;

import com.gmoi.directmessage.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUser1OrUser2(User user1, User user2);

    boolean existsByUser1AndUser2(User user1, User user2);
}
