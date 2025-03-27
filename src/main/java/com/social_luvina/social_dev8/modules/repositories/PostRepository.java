package com.social_luvina.social_dev8.modules.repositories;

import org.springframework.stereotype.Repository;

// import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.social_luvina.social_dev8.modules.models.entities.Post;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.models.enums.PostStatus;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  
  @Query("SELECT p FROM Post p WHERE (p.user = :currentUser OR (p.user IN :friends AND p.postStatus <> :privateStatus)) ORDER BY p.createdAt DESC")
  Page<Post> findRecentPostsByUsersAndSelf(@Param("friends") List<User> friends, @Param("currentUser") User currentUser,@Param("privateStatus") PostStatus privateStatus, Pageable pageable);

  int countByUserIdAndCreatedAtBetween(long userId, LocalDateTime startDate, LocalDateTime endDate);
}
