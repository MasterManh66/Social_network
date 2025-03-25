package com.social_luvina.social_dev8.modules.services.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.social_luvina.social_dev8.modules.models.dto.request.FriendRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.FriendResponse;

public interface FriendServiceInterface {
  ResponseEntity<ApiResponse<FriendResponse>> sendFriendRequest(Authentication authentication, FriendRequest request);
  ResponseEntity<ApiResponse<FriendResponse>> acceptFriendRequest(Authentication authentication, FriendRequest request);
  ResponseEntity<ApiResponse<Void>> declineFriendRequest(Authentication authentication, FriendRequest request);
  ResponseEntity<ApiResponse<Void>> deleteFriend(Authentication authentication, FriendRequest request);
}
