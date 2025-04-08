package com.social_luvina.social_dev8.modules.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @Operation(summary = "Decline Friend user receiver", description = "Decline a pending friend user receiver")
  @DeleteMapping("/decline/{friendIdToDecline}")
  public ResponseEntity<ApiResponse<Void>> declineFriendRequest(Authentication authentication, @PathVariable("friendIdToDecline") long friendIdToDecline) {
      return friendService.declineFriend(authentication, friendIdToDecline);
  }

  @Operation(summary = "Delete Friend", description = "Remove a friend from friend list")
  @DeleteMapping("/delete/{friendIdToDelete}")
  public ResponseEntity<ApiResponse<Void>> deleteFriend(Authentication authentication, @PathVariable("friendIdToDelete") long friendIdToDelete) {
      return friendService.deleteFriend(authentication, friendIdToDelete);
  }

  @Operation(summary = "User's friends list", description = "User's friends list")
  @GetMapping("/listFriend")
  public ResponseEntity<ApiResponse<List<UserResponse>>> getListFriend(Authentication authentication) {
        return friendService.getListFriend(authentication);
  }

  @Operation(summary = "List of friends sent", description = "List of friends sent")
  @GetMapping("/listSend")
  public ResponseEntity<ApiResponse<List<FriendResponse>>> getListSend(Authentication authentication) {
    return friendService.getListSend(authentication);
  }

  @Operation(summary = "List of friends receiver", description = "List of friends receiver")
  @GetMapping("/listReceiver")
  public ResponseEntity<ApiResponse<List<FriendResponse>>> getListReceiver(Authentication authentication) {
    return friendService.getListReceiver(authentication);
  }
}
