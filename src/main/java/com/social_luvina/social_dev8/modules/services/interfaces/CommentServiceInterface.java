package com.social_luvina.social_dev8.modules.services.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.social_luvina.social_dev8.modules.models.dto.request.CommentRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.CommentResponse;

public interface CommentServiceInterface {
    ResponseEntity<ApiResponse<CommentResponse>> createComment(Authentication authentication, long postId, CommentRequest request);
    ResponseEntity<ApiResponse<CommentResponse>> editComment(Authentication authentication, long commentId, CommentRequest request);
    ResponseEntity<ApiResponse<Void>> deleteComment(Authentication authentication, long commentId);
}
