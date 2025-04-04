package com.social_luvina.social_dev8.modules.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.social_luvina.social_dev8.modules.models.entities.Comment;
import com.social_luvina.social_dev8.modules.models.entities.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    int countByUserIdAndCreatedAtBetween(long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("""
        SELECT COUNT(c) 
        FROM Comment c 
        WHERE c.post.id IN :postId
        AND c.createdAt BETWEEN :startDate AND :endDate
    """)
    int countByPostIdsAndCreatedAtBetween(@Param("postId") List<Long> postId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    Page<Comment> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
