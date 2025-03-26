package com.social_luvina.social_dev8.modules.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.social_luvina.social_dev8.modules.models.entities.Friend;
import com.social_luvina.social_dev8.modules.models.entities.User;
// import com.social_luvina.social_dev8.modules.models.enums.FriendStatus;
import com.social_luvina.social_dev8.modules.models.enums.FriendStatus;

import java.time.LocalDateTime;
import java.util.List;
// import java.time.LocalDateTime;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long>{

  @Query("SELECT f.receiver FROM Friend f WHERE f.requester = :user AND f.friendStatus = 'ACCEPTED' " +"UNION " +
           "SELECT f.requester FROM Friend f WHERE f.receiver = :user AND f.friendStatus = 'ACCEPTED'")
  List<User> findAllFriends(@Param("user") User user);
  
  Optional<Friend> findByRequesterAndReceiver(User requester, User receiver);

  int countByRequesterIdAndFriendStatusAndUpdatedAtBetween(long requesterId, FriendStatus status, LocalDateTime startDate, LocalDateTime endDate);
  int countByReceiverIdAndFriendStatusAndUpdatedAtBetween(long receiverId, FriendStatus status, LocalDateTime startDate, LocalDateTime endDate);  
}
