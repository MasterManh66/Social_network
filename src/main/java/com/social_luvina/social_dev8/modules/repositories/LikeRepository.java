package com.social_luvina.social_dev8.modules.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.social_luvina.social_dev8.modules.models.entities.Like;
import com.social_luvina.social_dev8.modules.models.entities.User;

public interface LikeRepository extends JpaRepository<Like, Long>{
  Like findByUserIdAndPostId(Long userId, Long postId);
  int countByUserIdAndCreatedAtBetween(long userId, LocalDateTime startDate, LocalDateTime endDate);

  @Query("""
    SELECT COUNT(l) 
    FROM Like l 
    WHERE l.post.id IN :postId
    AND l.createdAt BETWEEN :startDate AND :endDate
  """)
  int countByPostIdsAndCreatedAtBetween(@Param("postId") List<Long> postId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

  Page<Like> findByUserOrderByCreatedAt(User user, Pageable pageable);
}
