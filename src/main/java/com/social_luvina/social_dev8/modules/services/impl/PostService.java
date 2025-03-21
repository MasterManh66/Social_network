package com.social_luvina.social_dev8.modules.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.social_luvina.social_dev8.modules.models.dto.request.PostRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.PostResponse;
import com.social_luvina.social_dev8.modules.models.entities.Post;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.repositories.PostRepository;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
import com.social_luvina.social_dev8.modules.services.interfaces.PostServiceInterface;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService implements PostServiceInterface {
    
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final ImageService imageService;

  private User getAuthenticatedUser(Authentication authentication) {
    return userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new BadCredentialsException("User is not found"));
  }

  private Post getPost(long postId) {
    return postRepository.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));
  }


  @Override
  public ResponseEntity<ApiResponse<PostResponse>> createPost(Authentication authentication, PostRequest request) {
    if (request.getContent() == null && request.getTitle() == null && request.getImages() == null) {
      throw new BadCredentialsException("Post is required a content or a title or images");
    }

    User user = getAuthenticatedUser(authentication);

    List<String> imagePaths = request.getImages() != null ? imageService.saveImage(request.getImages()) : null;
    Post post;
    try {
      post = Post.builder()
        .title(request.getTitle())
        .content(request.getContent())
        .createdAt(LocalDateTime.now())
        .postStatus(request.getPostStatus())
        .images(imagePaths)
        .user(user)
        .build();

      postRepository.save(post);
      
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(
        ApiResponse.<PostResponse>builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message("Tạo bài viết thất bại: " + e.getMessage())
            .build()
      );
    }

    PostResponse postResponse = new PostResponse(
        post.getId(),
        post.getUser().getId(),
        post.getTitle(),
        post.getContent(),
        post.getCreatedAt(),
        post.getPostStatus(),
        post.getImages()
    );

    return ResponseEntity.ok(
        ApiResponse.<PostResponse>builder()
            .status(HttpStatus.OK.value())
            .message("Tạo bài viết thành công!")
            .data(postResponse)
            .build()
    );
  }

  @Override
  public ResponseEntity<ApiResponse<PostResponse>> editPost(Authentication authentication, long postId, PostRequest request) {
    if (request.getContent() == null && request.getTitle() == null && request.getImages() == null) {
      throw new BadCredentialsException("Post requires at least a title, content, or images.");
    }
    Post post = getPost(postId);

    User user = getAuthenticatedUser(authentication);
    if (post.getUser().getId() != user.getId()) {
      throw new BadCredentialsException("You do not have permission to edit this post.");
    }

    post.setTitle(request.getTitle());
    post.setContent(request.getContent());
    post.setPostStatus(request.getPostStatus());
    post.setUpdatedAt(LocalDateTime.now());
    
    if (request.getImages() != null) {
      post.setImages(request.getImages());
    }

    postRepository.save(post);

    PostResponse postResponse = new PostResponse(
      post.getId(),
      post.getUser().getId(),
      post.getTitle(),
      post.getContent(),
      post.getCreatedAt(),
      post.getPostStatus(),
      post.getImages()
    );

    ApiResponse<PostResponse> apiResponse = ApiResponse.<PostResponse>builder()
              .message("Edit post completed")
              .data(postResponse)
              .build();
      return ResponseEntity.ok(apiResponse);
    }

  @Override
  public ResponseEntity<ApiResponse<Void>> deletePost(Authentication authentication, long postId) {
    Post post = getPost(postId);

    User user = getAuthenticatedUser(authentication);
    if (post.getUser().getId() != user.getId()) {
      throw new BadCredentialsException("You do not have permission to delete this post.");
    }

    if(post.getImages()!=null){
        for(String path : post.getImages()){
            String sanitizedPath = path.replace("/", "");
            imageService.deleteImageFile(sanitizedPath);
        }
    }

    postRepository.delete(post);

    ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
        .message("Delete post completed")
        .build();
    return ResponseEntity.ok(apiResponse);
  }
}
