package com.social_luvina.social_dev8.modules.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social_luvina.social_dev8.modules.models.dto.request.LikeRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.LikeResponse;
import com.social_luvina.social_dev8.modules.services.interfaces.LikeServiceInterface;

@ExtendWith(MockitoExtension.class)
public class LikeControllerTest {
  @InjectMocks
  private LikeController likeController;

  @Mock
  private LikeServiceInterface likeService;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(likeController).build();
  }

  @Test
  void testCreateLike_Success() throws Exception {
    LikeRequest request = LikeRequest.builder().postId(2L).build();
    LikeResponse response = LikeResponse.builder().id(1L)
            .createdAt(LocalDateTime.of(2024, 3, 25, 14, 30, 0))
            .postId(2L)
            .userId(3L)
            .build();

    ApiResponse<LikeResponse> apiResponse = ApiResponse.<LikeResponse>builder().data(response).message("Like post successfully").build();

    when(likeService.createLike(any(), anyLong(), any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(post("/api/like/create")
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void testDeleteLike_Success() throws Exception{
    ApiResponse<Void> apiResponse = ApiResponse.<Void>builder().message("Delete Like successfully").build();

    when(likeService.unLike(any(), anyLong())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(delete("/api/like/delete/{postId}",2)
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }
}
