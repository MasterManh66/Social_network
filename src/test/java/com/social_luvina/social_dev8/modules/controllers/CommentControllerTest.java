package com.social_luvina.social_dev8.modules.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social_luvina.social_dev8.modules.models.dto.request.CommentRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.CommentResponse;
import com.social_luvina.social_dev8.modules.services.interfaces.CommentServiceInterface;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {
  @InjectMocks
  private CommentController commentController;

  @Mock
  private CommentServiceInterface commentService;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
  }

  @Test
  void testCreateComment_Success() throws Exception{
    CommentRequest request = CommentRequest.builder().postId(2L).images(Arrays.asList("image3.jpg")).content("I very like your post").build();
    CommentResponse response = CommentResponse.builder().id(2L)
        .content("I very like your post")
        .createdAt(LocalDateTime.of(2024, 3, 26, 16, 15, 0))
        .updatedAt(LocalDateTime.of(2024, 3, 27, 18, 45, 0))
        .userId(2L)
        .images(Arrays.asList("image3.jpg"))
        .postId(2L)
        .build();
    ApiResponse<CommentResponse> apiResponse = ApiResponse.<CommentResponse>builder().data(response).build();

    when(commentService.createComment(any(), anyLong(), any())).thenReturn(ResponseEntity.ok(apiResponse));

     mockMvc.perform(post("/api/comment/create")
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  void testEditComment_Success() throws Exception{
    CommentRequest request = CommentRequest.builder().postId(2L).images(Arrays.asList("image3.jpg")).content("You need more content").build();
    CommentResponse response = CommentResponse.builder().id(2L)
        .content("You need more content")
        .createdAt(LocalDateTime.of(2024, 3, 26, 16, 15, 0))
        .updatedAt(LocalDateTime.of(2024, 3, 27, 18, 45, 0))
        .userId(2L)
        .images(Arrays.asList("image3.jpg"))
        .postId(2L)
        .build();
    ApiResponse<CommentResponse> apiResponse = ApiResponse.<CommentResponse>builder().data(response).build();

    when(commentService.editComment(any(), anyLong(), any())).thenReturn(ResponseEntity.ok(apiResponse));
     mockMvc.perform(put("/api/comment/edit/{commentId}",2)
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  void testDeleteComment_Success() throws Exception{
    ApiResponse<Void> apiResponse = ApiResponse.<Void>builder().message("Delete Comment successfully").build();

    when(commentService.deleteComment(any(), anyLong())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(delete("/api/comment/delete/{commentId}",2)
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Delete Comment successfully"));
  }
}
