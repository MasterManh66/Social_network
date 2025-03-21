package com.social_luvina.social_dev8.modules.repositories;

import org.springframework.stereotype.Repository;

// import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.social_luvina.social_dev8.modules.models.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  Page<Post> findByUserIdOrderByCreatedAtDesc(long userId, Pageable pageable);
}
