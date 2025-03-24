package com.social_luvina.social_dev8.modules.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social_luvina.social_dev8.modules.models.entities.Friend;
import com.social_luvina.social_dev8.modules.models.entities.User;
// import com.social_luvina.social_dev8.modules.models.enums.FriendStatus;

// import java.time.LocalDateTime;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long>{
  Optional<Friend> findByRequesterAndReceiver(User requester, User receiver);
  // int countByRequesterAndFriendStatusAndUpdatedAtBetween(User requester, FriendStatus status, LocalDateTime startDate, LocalDateTime endDate);
  // int countByReceiverAndFriendStatusAndUpdatedAtBetween(User receiver, FriendStatus status, LocalDateTime startDate, LocalDateTime endDate);
}
