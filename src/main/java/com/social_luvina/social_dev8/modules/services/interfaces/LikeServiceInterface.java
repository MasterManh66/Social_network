package com.social_luvina.social_dev8.modules.services.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.social_luvina.social_dev8.modules.models.dto.request.LikeRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.LikeResponse;

public interface LikeServiceInterface {
  ResponseEntity<ApiResponse<LikeResponse>> createLike(Authentication authentication, long postId, LikeRequest request);
  ResponseEntity<ApiResponse<Void>> unLike(Authentication authentication, long postId);
}
