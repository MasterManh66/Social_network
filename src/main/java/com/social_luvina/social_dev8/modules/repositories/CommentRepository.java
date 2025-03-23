package com.social_luvina.social_dev8.modules.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.social_luvina.social_dev8.modules.models.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    int countByUserIdAndCreatedAtBetween(long userId, LocalDateTime startDate, LocalDateTime endDate);
}
