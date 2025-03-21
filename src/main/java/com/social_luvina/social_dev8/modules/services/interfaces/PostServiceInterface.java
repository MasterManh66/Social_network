package com.social_luvina.social_dev8.modules.services.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.social_luvina.social_dev8.modules.models.dto.request.PostRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.PostResponse;

public interface PostServiceInterface {
  ResponseEntity<ApiResponse<PostResponse>> createPost(Authentication authentication, PostRequest request);
  // ResponseEntity<ApiResponse<PostResponse>> getPostById(Authentication authentication,long postId);
  ResponseEntity<ApiResponse<PostResponse>> editPost(Authentication authentication, long postId, PostRequest request);
  // ResponseEntity<ApiResponse<Void>> deletePost(Authentication authentication, long postId);
}
