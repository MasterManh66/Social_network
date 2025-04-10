package com.social_luvina.social_dev8.modules.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social_luvina.social_dev8.modules.models.dto.request.FriendRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.FriendResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserResponse;
import com.social_luvina.social_dev8.modules.models.enums.FriendStatus;
import com.social_luvina.social_dev8.modules.services.interfaces.FriendServiceInterface;

@ExtendWith(MockitoExtension.class)
public class FriendControllerTest {
  @InjectMocks
  private FriendController friendController;

  @Mock
  private FriendServiceInterface friendService;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(friendController).build();
  }

  @Test
  void testSendFriend_Success() throws Exception{
    FriendRequest request = FriendRequest.builder().receiverId(3L).build();
    FriendResponse response = FriendResponse.builder()
            .userId(1L)
            .fullName("Rose Nguyen")
            .avatar("avatar1.jpg")
            .status(FriendStatus.PENDING) 
            .createdAt(LocalDateTime.of(2025, 3, 25, 14, 30, 0))
            .build();
    ApiResponse<FriendResponse> apiResponse = ApiResponse.<FriendResponse>builder().data(response).message("Send successfully").build();

    when(friendService.sendFriendRequest(any(), any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(post("/api/friend/send")
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void testAcceptFriend_Success() throws Exception{
    FriendRequest request = FriendRequest.builder().receiverId(3).build();
    FriendResponse response = FriendResponse.builder()
            .userId(1L)
            .fullName("Rose Nguyen")
            .avatar("avatar1.jpg")
            .status(FriendStatus.PENDING) 
            .createdAt(LocalDateTime.of(2025, 3, 25, 14, 30, 0))
            .build();
    ApiResponse<FriendResponse> apiResponse = ApiResponse.<FriendResponse>builder().data(response).message("Send successfully").build();

    when(friendService.acceptFriendRequest(any(), any())) .thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(post("/api/friend/accept")
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void testDeclineFriend_Success() throws Exception{
    ApiResponse<Void> apiResponse = ApiResponse.<Void>builder().message("Decline Friend successfully").build();

    when(friendService.declineFriend(any(), anyLong())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(delete("/api/friend/decline/{friendIdToDecline}", 3)
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void testDeleteFriend_Success() throws Exception{
    ApiResponse<Void> apiResponse = ApiResponse.<Void>builder().message("Delete Friend successfully").build();

    when(friendService.deleteFriend(any(), anyLong())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(delete("/api/friend/delete/{friendIdToDelete}", 2)
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void testGetListFriend_Success() throws Exception{
    List<UserResponse> responses = new ArrayList<>();
    ApiResponse<List<UserResponse>> apiResponse = ApiResponse.<List<UserResponse>>builder().data(responses).message("Get List Friend successfully").build();

    when(friendService.getListFriend(any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(get("/api/friend/listFriend")
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void testGetListSend_Success() throws Exception{
    List<FriendResponse> responses = new ArrayList<>();
    ApiResponse<List<FriendResponse>> apiResponse = ApiResponse.<List<FriendResponse>>builder().data(responses).message("Get List Send successfully").build();

    when(friendService.getListSend(any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(get("/api/friend/listSend")
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void testGetListReceiver_Success() throws Exception{
    List<FriendResponse> responses = new ArrayList<>();
    ApiResponse<List<FriendResponse>> apiResponse = ApiResponse.<List<FriendResponse>>builder().data(responses).message("Get List Receiver successfully").build();

    when(friendService.getListReceiver(any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(get("/api/friend/listReceiver")
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }
}
