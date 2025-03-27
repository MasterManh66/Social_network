package com.social_luvina.social_dev8.modules.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.social_luvina.social_dev8.modules.exception.CustomException;
import com.social_luvina.social_dev8.modules.models.dto.request.PostRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.PostResponse;
import com.social_luvina.social_dev8.modules.models.entities.Post;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.models.enums.PostStatus;
import com.social_luvina.social_dev8.modules.repositories.FriendRepository;
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
  private final FriendRepository friendRepository;

  private User getAuthenticatedUser(Authentication authentication) {
    return userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new BadCredentialsException("User không tồn tại"));
  }

  private Post getPost(long postId) {
    return postRepository.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));
  }


  @Override
  public ResponseEntity<ApiResponse<PostResponse>> createPost(Authentication authentication, PostRequest request) {
    if (request.getContent() == null && request.getTitle() == null && request.getImages() == null) {
      throw new CustomException("Post is required a content or a title or images", HttpStatus.BAD_REQUEST);
    }

    User user = getAuthenticatedUser(authentication);

    List<String> imagePaths = request.getImages() != null ? imageService.saveImage(request.getImages()) : null;
    
    Post post = Post.builder()
        .title(request.getTitle())
        .content(request.getContent())
        .createdAt(LocalDateTime.now())
        .postStatus(request.getPostStatus())
        .images(imagePaths)
        .user(user)
        .build();

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

    return ResponseEntity.ok(
        ApiResponse.<PostResponse>builder()
            .status(HttpStatus.CREATED.value())
            .message("Tạo bài viết thành công!")
            .data(postResponse)
            .build()
    );
  }

  @Override
  public ResponseEntity<ApiResponse<PostResponse>> editPost(Authentication authentication, long postId, PostRequest request) {
    Post post = getPost(postId);

    User user = getAuthenticatedUser(authentication);
    if (post.getUser().getId() != user.getId()) {
      throw new CustomException("Bạn không có quyền chỉnh sửa bài viết này.", HttpStatus.FORBIDDEN);
    }

    boolean isUpdated = false;
    if (request.getTitle() != null && !request.getTitle().equals(post.getTitle())){
      post.setTitle(request.getTitle());
      isUpdated = true;
    }
    if (request.getContent() != null && !request.getContent().equals(post.getContent())){
      post.setContent(request.getContent());
      isUpdated = true;
    }
    if (request.getPostStatus() != null && !request.getPostStatus().equals(post.getPostStatus())){
      post.setPostStatus(request.getPostStatus());
      isUpdated = true;
    }
    post.setUpdatedAt(LocalDateTime.now());
    if (request.getImages() != null && !request.getImages().equals(post.getImages())) {
      post.setImages(request.getImages());
      isUpdated = true;
    }

    if (isUpdated){
      postRepository.save(post);
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
      throw new CustomException("Bạn không có quyền xoá bài viết này.", HttpStatus.FORBIDDEN);
    }

    if (post.getImages() != null){
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

  @Override
  public ResponseEntity<ApiResponse<List<PostResponse>>> getTimeline(Authentication authentication) {
      User user = getAuthenticatedUser(authentication);

      List<User> friends = friendRepository.findAllFriends(user);

      if (friends.isEmpty()) {
        friends = new ArrayList<>();
      }
      friends.add(user);

      Page<Post> posts = postRepository.findRecentPostsByUsersAndSelf(friends, user, PostStatus.PRIVATE, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")));

      List<PostResponse> postResponses = posts.getContent().stream().map(post -> 
          PostResponse.builder()
              .id(post.getId())
              .userId(post.getUser().getId())
              .title(post.getTitle())
              .content(post.getContent())
              .createdAt(post.getCreatedAt())
              .postStatus(post.getPostStatus())
              .images(post.getImages())
              .build()
      ).toList();


      return ResponseEntity.ok(
            ApiResponse.<List<PostResponse>>builder()
                  .status(HttpStatus.OK.value())
                  .message("Lấy timeline thành công!")
                  .data(postResponses)
                  .build()
      );
  }
}
