package com.social_luvina.social_dev8.modules.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.social_luvina.social_dev8.modules.exception.CustomException;
import com.social_luvina.social_dev8.modules.models.dto.request.FriendRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.FriendResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserResponse;
import com.social_luvina.social_dev8.modules.models.entities.Friend;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.models.enums.FriendStatus;
import com.social_luvina.social_dev8.modules.repositories.FriendRepository;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
import com.social_luvina.social_dev8.modules.services.interfaces.FriendServiceInterface;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FriendService implements FriendServiceInterface{
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    private User getAuthenticatedUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
          .orElseThrow(() -> new CustomException("Người dùng không tồn tại", HttpStatus.NOT_FOUND));
    }

    private User getFriendIdToDelete(long friendIdToDelete){
        return userRepository.findById(friendIdToDelete)
          .orElseThrow(() -> new CustomException("Bạn bè không tồn tại", HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<ApiResponse<List<UserResponse>>> getListFriend(Authentication authentication){
        User user = getAuthenticatedUser(authentication);

        List<User> friends = friendRepository.findAllFriends(user);

        List<UserResponse> friendResponses = friends.stream()
            .map(UserResponse::new)
            .toList();

        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
            .status(HttpStatus.OK.value())
            .message(friendResponses.isEmpty() ? "Bạn chưa có bạn bè nào!" : "Lấy danh sách bạn bè của bạn thành công!")
            .data(friendResponses)
            .build());
    }

    @Override
    public ResponseEntity<ApiResponse<FriendResponse>> sendFriendRequest(Authentication authentication, FriendRequest request) {
      User user = getAuthenticatedUser(authentication);
      User receiver = userRepository.findById(request.getReceiverId())
            .orElseThrow(() -> new CustomException("Người dùng không tồn tại", HttpStatus.NOT_FOUND));
  
      if (user.equals(receiver)) {
          throw new CustomException("Bạn không thể gửi lời mời cho chính mình", HttpStatus.CONFLICT);
      }
  
      // Kiểm tra xem đã có lời mời từ user chưa
      Optional<Friend> existingFriend = friendRepository.findByRequesterAndReceiver(user, receiver);
      if (existingFriend.isPresent()) {
          if (existingFriend.get().getFriendStatus() == FriendStatus.ACCEPTED) {
              throw new CustomException("Đã là bạn bè", HttpStatus.BAD_REQUEST);
          }
          throw new CustomException("Yêu cầu kết bạn đã được gửi", HttpStatus.OK);
      }
  
      // Kiểm tra nếu receiver đã gửi lời mời trước → Chấp nhận lời mời
      Optional<Friend> reverseFriend = friendRepository.findByRequesterAndReceiver(receiver, user);
      if (reverseFriend.isPresent() && reverseFriend.get().getFriendStatus() == FriendStatus.PENDING) {
          Friend updatedFriend = reverseFriend.get();
          updatedFriend.setFriendStatus(FriendStatus.ACCEPTED);
          friendRepository.save(updatedFriend);
  
          FriendResponse friendResponse = FriendResponse.builder()
                  .userId(receiver.getId())
                  .fullName(receiver.getFirstName() + " " + receiver.getLastName())
                  .avatar(receiver.getAvatar())
                  .status(FriendStatus.ACCEPTED)
                  .createdAt(updatedFriend.getCreatedAt())
                  .build();
  
          return ResponseEntity.ok(
              ApiResponse.<FriendResponse>builder()
                  .status(HttpStatus.OK.value())
                  .message("Lời mời kết bạn đã được chấp nhận!")
                  .data(friendResponse)
                  .build()
          );
      }
  
      Friend newFriend = Friend.builder()
              .requester(user)
              .receiver(receiver)
              .friendStatus(FriendStatus.PENDING)
              .createdAt(LocalDateTime.now())
              .build();
      friendRepository.save(newFriend);
  
      FriendResponse friendResponse = FriendResponse.builder()
              .userId(receiver.getId())
              .fullName(receiver.getFirstName() + " " + receiver.getLastName())
              .avatar(receiver.getAvatar())
              .status(FriendStatus.PENDING)
              .createdAt(newFriend.getCreatedAt())
              .build();
  
      return ResponseEntity.ok(
          ApiResponse.<FriendResponse>builder()
              .status(HttpStatus.OK.value())
              .message("Gửi lời mời kết bạn thành công!")
              .data(friendResponse)
              .build()
      );    
    }

    @Override
    public ResponseEntity<ApiResponse<FriendResponse>> acceptFriendRequest(Authentication authentication, FriendRequest request) {
        User user = getAuthenticatedUser(authentication);
        User sender = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        Optional<Friend> friendRequest = friendRepository.findByRequesterAndReceiver(sender, user);
        if (friendRequest.isEmpty() || friendRequest.get().getFriendStatus() != FriendStatus.PENDING) {
            throw new CustomException("No pending friend request found", HttpStatus.BAD_REQUEST);
        }

        Friend friend = friendRequest.get();
        friend.setFriendStatus(FriendStatus.ACCEPTED);
        friendRepository.save(friend);

        FriendResponse friendResponse = FriendResponse.builder()
                .userId(sender.getId())
                .fullName(sender.getFirstName() + " " + sender.getLastName())
                .avatar(sender.getAvatar())
                .status(FriendStatus.ACCEPTED)
                .createdAt(friend.getCreatedAt())
                .build();

        return ResponseEntity.ok(
                ApiResponse.<FriendResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Bạn đã chấp nhận yêu cầu kết bạn từ " + sender.getFirstName() + " " + sender.getLastName())
                        .data(friendResponse)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> declineFriend(Authentication authentication, long friendIdToDecline) {
        User user = getAuthenticatedUser(authentication);
        User sender = userRepository.findById(friendIdToDecline)
                .orElseThrow(() -> new CustomException("Người dùng không tồn tại", HttpStatus.NOT_FOUND));

        Optional<Friend> friendRequest = friendRepository.findByRequesterAndReceiver(sender, user);

        if (friendRequest.isEmpty() || friendRequest.get().getFriendStatus() != FriendStatus.PENDING) {
            throw new CustomException("Không tìm thấy yêu cầu kết bạn đang chờ xử lý", HttpStatus.NOT_FOUND);
        }

        friendRepository.delete(friendRequest.get());

        return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Bạn đã từ chối yêu cầu kết bạn nhận được từ " + sender.getFirstName() + " " + sender.getLastName())
                .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteFriend(Authentication authentication, long friendIdToDelete) {
        User user = getAuthenticatedUser(authentication);
        User friend = getFriendIdToDelete(friendIdToDelete);

        Optional<Friend> friendship = friendRepository.findByRequesterAndReceiver(user, friend);
        Optional<Friend> reverseFriendship = friendRepository.findByRequesterAndReceiver(friend, user);

        if (friendship.isEmpty() && reverseFriendship.isEmpty()) {
            throw new CustomException("Các bạn không phải bạn bè", HttpStatus.NOT_FOUND);
        }

        friendship.ifPresent(friendRepository::delete);
        reverseFriendship.ifPresent(friendRepository::delete);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(HttpStatus.NO_CONTENT.value())
                        .message("Huỷ kết bạn thành công với " + friend.getFirstName() + " " + friend.getLastName())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<List<FriendResponse>>> getListSend(Authentication authentication){
        User user = getAuthenticatedUser(authentication);
        List<Friend> friends = friendRepository.findAllPendingFriendRequests(user);

        List<FriendResponse> friendResponses = friends.stream().map(friend -> {
            User friendUser = friend.getReceiver().equals(user) ? friend.getRequester() : friend.getReceiver();
            return FriendResponse.builder()
                .userId(friendUser.getId())
                .fullName(friendUser.getFirstName() + " " + friendUser.getLastName())
                .avatar(friendUser.getAvatar())
                .status(friend.getFriendStatus())
                .createdAt(friend.getCreatedAt())
                .build();
            }).toList();

        return ResponseEntity.ok(
            ApiResponse.<List<FriendResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(friendResponses.isEmpty() ? "Bạn chưa gửi lời mời kết bạn nào!" : "Lấy thành công các lời mời kết bạn của bạn đã gửi đi!")
                .data(friendResponses)
                .build(
        ));   
    }

    @Override
    public ResponseEntity<ApiResponse<List<FriendResponse>>> getListReceiver(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        List<Friend> friends = friendRepository.findAllPendingFriendReceiver(user);

        List<FriendResponse> friendResponses = friends.stream().map(friend -> {
            User friendUser = friend.getReceiver().equals(user) ? friend.getRequester() : friend.getReceiver();
            return FriendResponse.builder()
                .userId(friendUser.getId())
                .fullName(friendUser.getFirstName() + " " + friendUser.getLastName())
                .avatar(friendUser.getAvatar())
                .status(friend.getFriendStatus())
                .createdAt(friend.getCreatedAt())
                .build();
            }).toList();
        return ResponseEntity.ok(
            ApiResponse.<List<FriendResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(friendResponses.isEmpty() ? "Bạn chưa nhận được lời mời kết bạn nào!" : "Lấy thành công các lời mời kết bạn của bạn đã nhận!")
                .data(friendResponses)
                .build()
        );
    }
}
