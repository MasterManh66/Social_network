package com.social_luvina.social_dev8.modules.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social_luvina.social_dev8.modules.models.dto.request.FriendRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.FriendResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserResponse;
import com.social_luvina.social_dev8.modules.services.interfaces.FriendServiceInterface;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("/api/friend")
public class FriendController {
  @Autowired
  private FriendServiceInterface friendService;

  @Operation(summary = "Send Friend Request", description = "Send Friend Request")
  @PostMapping("/send")
  public ResponseEntity<ApiResponse<FriendResponse>> sendFriendRequest(Authentication authentication,@Validated @RequestBody FriendRequest request){
      return friendService.sendFriendRequest(authentication,request);
  }

  @Operation(summary = "Accept Friend Request", description = "Accept a pending friend request")
  @PostMapping("/accept")
  public ResponseEntity<ApiResponse<FriendResponse>> acceptFriendRequest(Authentication authentication, @Validated @RequestBody FriendRequest request) {
      return friendService.acceptFriendRequest(authentication, request);
  }

  @Operation(summary = "Decline Friend Request", description = "Decline a pending friend request")
  @PostMapping("/decline")
  public ResponseEntity<ApiResponse<Void>> declineFriendRequest(Authentication authentication, @Validated @RequestBody FriendRequest request) {
      return friendService.declineFriendRequest(authentication, request);
  }

  @Operation(summary = "Delete Friend", description = "Remove a friend from friend list")
  @PostMapping("/delete")
  public ResponseEntity<ApiResponse<Void>> deleteFriend(Authentication authentication, @Validated @RequestBody FriendRequest request) {
      return friendService.deleteFriend(authentication, request);
  }

  @PostMapping("/list")
  public ResponseEntity<ApiResponse<List<UserResponse>>> getListFriend(Authentication authentication) {
        return friendService.getListFriend(authentication);
  }
}
