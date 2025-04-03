package com.social_luvina.social_dev8.modules.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.social_luvina.social_dev8.modules.models.dto.request.PostRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.PostSearchRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.PostResponse;
import com.social_luvina.social_dev8.modules.services.interfaces.PostServiceInterface;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/posts")
public class PostController {

  private final PostServiceInterface postService;

  public PostController(
    PostServiceInterface postService) {
    this.postService = postService;
  }
  
  @Operation(summary = "Create Post", description = "Create a new Post")
  @PostMapping("/create")
  public ResponseEntity<ApiResponse<PostResponse>> createPost(Authentication authentication, @Validated @RequestBody PostRequest request){
    return postService.createPost(authentication, request);
  }

  @Operation(summary = "Edit Post", description = "Edit Post")
  @PutMapping("/edit/{postId}")
  public ResponseEntity<ApiResponse<PostResponse>> editPost(Authentication authentication, @PathVariable("postId") long postId, @Validated @RequestBody PostRequest request){
    return postService.editPost(authentication,postId,request); 
  }

  @Operation(summary = "Delete Post", description = "Delete Post")
  @DeleteMapping("/delete/{postId}")
  public ResponseEntity<ApiResponse<Void>> deletePost(Authentication authentication, @PathVariable("postId") long postId){
    return postService.deletePost(authentication,postId);
  }

  @PostMapping("/searchPost")
  public ResponseEntity<ApiResponse<List<PostResponse>>> searchPost(Authentication authentication, @Validated @RequestBody PostSearchRequest request) {
      return postService.searchPost(authentication, request);
  }
  
  @Operation(summary = "Get Timeline", description = "Get recent posts from friends")
  @GetMapping("/timeline")
  public ResponseEntity<ApiResponse<List<PostResponse>>> getTimeline(Authentication authentication) {
    return postService.getTimeline(authentication);
  }

  @GetMapping("/postUser")
  public ResponseEntity<ApiResponse<List<PostResponse>>> getPostUser(Authentication authentication) {
      return postService.getPostUser(authentication);
  }
  
}
