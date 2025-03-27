package com.social_luvina.social_dev8.modules.services.impl;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.social_luvina.social_dev8.modules.exception.CustomException;
import com.social_luvina.social_dev8.modules.models.dto.request.LikeRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.LikeResponse;
import com.social_luvina.social_dev8.modules.models.entities.Like;
import com.social_luvina.social_dev8.modules.models.entities.Post;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.models.enums.PostStatus;
import com.social_luvina.social_dev8.modules.repositories.LikeRepository;
import com.social_luvina.social_dev8.modules.repositories.PostRepository;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
import com.social_luvina.social_dev8.modules.services.interfaces.LikeServiceInterface;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LikeService implements LikeServiceInterface{
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final LikeRepository likeRepository;

  private User getAuthenticatedUser(Authentication authentication) {
    return userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new BadCredentialsException("User không tồn tại"));
  }

  private Like findExistingLike(long userId, long postId) {
    return likeRepository.findByUserIdAndPostId(userId, postId);
  }


  @Override
  public ResponseEntity<ApiResponse<LikeResponse>> createLike(Authentication authentication, long postId, LikeRequest request){
    Post post = postRepository.findById(postId)
      .orElseThrow(() -> new CustomException("Bài viết không tồn tại", HttpStatus.NOT_FOUND));

    User user = getAuthenticatedUser(authentication);
    
    User auth = post.getUser();
    if (user.getId() != auth.getId() && post.getPostStatus().equals(PostStatus.PRIVATE)) {
        throw new CustomException("You can not see the article this post!", HttpStatus.FORBIDDEN);
    }

    if (findExistingLike(user.getId(), postId) != null) {
      throw new CustomException("Bạn đã Like bài viết này trước đó!", HttpStatus.CONFLICT);
    }

    Like like = Like.builder()
      .createdAt(LocalDateTime.now())
      .user(user)
      .post(post)
      .build();
    likeRepository.save(like);

    LikeResponse likeResponse = new LikeResponse(
      like.getId(),
      like.getCreatedAt(),
      like.getPost().getId(),
      like.getUser().getId()
    );

    return ResponseEntity.ok(
            ApiResponse.<LikeResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Like bài viết thành công!")
                .data(likeResponse)
                .build()
    );
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> unLike(Authentication authentication, long postId){
    Post post = postRepository.findById(postId)
      .orElseThrow(() -> new CustomException("The post is not found", HttpStatus.NOT_FOUND));

    User user = getAuthenticatedUser(authentication);
    
    User auth = post.getUser();
    if (user.getId() != auth.getId() && post.getPostStatus().equals(PostStatus.PRIVATE)) {
        throw new CustomException("You can not see the article this post!", HttpStatus.FORBIDDEN);
    }

    Like like = likeRepository.findByUserIdAndPostId(user.getId(), postId);

    if(like == null){
      throw new CustomException("Like post is not found!", HttpStatus.NOT_FOUND);
    }

    likeRepository.delete(like);

    ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
            .message("Un Like completed")
            .build();
    return ResponseEntity.ok(apiResponse);
  }
}
