package com.social_luvina.social_dev8.modules.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social_luvina.social_dev8.modules.models.entities.Like;

public interface LikeRepository extends JpaRepository<Like, Long>{
  Like findByUserIdAndPostId(Long userId, Long postId);
  int countByUserIdAndCreatedAtBetween(long userId, LocalDateTime startDate, LocalDateTime endDate);
}
