package com.social_luvina.social_dev8.modules.services.interfaces;

// import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.social_luvina.social_dev8.modules.models.dto.request.FriendRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.FriendResponse;
// import com.social_luvina.social_dev8.modules.models.dto.response.UserResponse;

public interface FriendServiceInterface {
  // ResponseEntity<ApiResponse<List<UserResponse>>> getListFriend(Authentication authentication, FriendRequest request);
  ResponseEntity<ApiResponse<FriendResponse>> sendFriendRequest(Authentication authentication, FriendRequest request);
  ResponseEntity<ApiResponse<FriendResponse>> acceptFriendRequest(Authentication authentication, FriendRequest request);
  ResponseEntity<ApiResponse<Void>> declineFriendRequest(Authentication authentication, FriendRequest request);
  ResponseEntity<ApiResponse<Void>> deleteFriend(Authentication authentication, FriendRequest request);
}
