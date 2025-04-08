package com.social_luvina.social_dev8.modules.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
import com.social_luvina.social_dev8.modules.models.dto.request.PostRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.PostSearchRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.PostResponse;
import com.social_luvina.social_dev8.modules.models.enums.PostStatus;
import com.social_luvina.social_dev8.modules.services.interfaces.PostServiceInterface;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
  @InjectMocks
  private PostController postController;

  @Mock
  private PostServiceInterface postService;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
  }

  @Test
  void testCreatePost_Success() throws Exception {
    PostRequest request = PostRequest.builder().title("learn test").content("write test for controller").postStatus(PostStatus.PUBLIC).images(Arrays.asList("image1.jpg", "image2.jpg")).build();
    PostResponse response = PostResponse.builder().userId(1L).createdAt(LocalDateTime.of(2024, 3, 25, 14, 30, 0)).title("learn test").content("write test for controller")
                                        .postStatus(PostStatus.PUBLIC).images(Arrays.asList("image1.jpg", "image2.jpg")).build();

    ApiResponse<PostResponse> apiResponse = ApiResponse.<PostResponse>builder().data(response).message("Create new post successfully").build();

    when(postService.createPost(any(), any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(post("/api/posts/create")
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Create new post successfully"));
  }

  @Test
  void testEditPost_Success() throws Exception {
    PostRequest request = PostRequest.builder().title("learn test").content("Edit post").postStatus(PostStatus.PUBLIC).images(Arrays.asList("image1.jpg", "image2.jpg")).build();
    PostResponse response = PostResponse.builder().userId(1L).createdAt(LocalDateTime.of(2024, 3, 25, 14, 30, 0)).title("learn test").content("Edit post")
                                        .postStatus(PostStatus.PUBLIC).images(Arrays.asList("image1.jpg", "image2.jpg")).build();

    ApiResponse<PostResponse> apiResponse = ApiResponse.<PostResponse>builder().data(response).message("Edit post successfully").build();

    when(postService.editPost(any(), anyLong(), any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(put("/api/posts/edit/{postId}",2)
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Edit post successfully"));
  }

  @Test
  void testDeletePost_Success() throws Exception{
    ApiResponse<Void> apiResponse = ApiResponse.<Void>builder().message("Delete Post successfully").build();

    when(postService.deletePost(any(), anyLong())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(delete("/api/posts/delete/{postId}",2)
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Delete Post successfully"));
  }

  @Test
  void testGetTimeline_Success() throws Exception{
    List<PostResponse> response = Arrays.asList(
      PostResponse.builder()
          .userId(1L)
          .createdAt(LocalDateTime.of(2024, 3, 25, 14, 30, 0))
          .title("learn test")
          .content("write test for controller")
          .postStatus(PostStatus.PUBLIC)
          .images(Arrays.asList("image1.jpg", "image2.jpg"))
          .build(),

      PostResponse.builder()
          .userId(2L)
          .createdAt(LocalDateTime.of(2024, 3, 26, 10, 15, 0))
          .title("another post")
          .content("another test case")
          .postStatus(PostStatus.PRIVATE)
          .images(Arrays.asList("image3.jpg"))
          .build()
      );
    
    ApiResponse<List<PostResponse>> apiResponse = ApiResponse.<List<PostResponse>>builder().data(response).message("Get Timeline Successfully").build();

    when(postService.getTimeline(any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(get("/api/posts/timeline")
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Get Timeline Successfully"));
  }

  @Test
  void testGetPostById_Success() throws Exception{
    List<PostResponse> response = Arrays.asList(
      PostResponse.builder()
          .userId(1L)
          .createdAt(LocalDateTime.of(2024, 3, 25, 14, 30, 0))
          .title("learn test")
          .content("write test for controller")
          .postStatus(PostStatus.PUBLIC)
          .images(Arrays.asList("image1.jpg", "image2.jpg"))
          .build(),

      PostResponse.builder()
          .userId(2L)
          .createdAt(LocalDateTime.of(2024, 3, 26, 10, 15, 0))
          .title("another post")
          .content("another test case")
          .postStatus(PostStatus.PRIVATE)
          .images(Arrays.asList("image3.jpg"))
          .build()
    );

    ApiResponse<List<PostResponse>> apiResponse = ApiResponse.<List<PostResponse>>builder().data(response).message("Get post user Successfully").build();

    when(postService.getPostUser(any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(get("/api/posts/postUser")
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Get post user Successfully"));
  }

  @Test
  void testSearchPost_Success() throws Exception {
    PostSearchRequest request = PostSearchRequest.builder().keyword("test").build();
    List<PostResponse> response = Arrays.asList(
      PostResponse.builder()
          .userId(1L)
          .createdAt(LocalDateTime.of(2024, 3, 25, 14, 30, 0))
          .title("learn test")
          .content("write test for controller")
          .postStatus(PostStatus.PUBLIC)
          .images(Arrays.asList("image1.jpg", "image2.jpg"))
          .build(),

      PostResponse.builder()
          .userId(2L)
          .createdAt(LocalDateTime.of(2024, 3, 26, 10, 15, 0))
          .title("another post")
          .content("another test case")
          .postStatus(PostStatus.PRIVATE)
          .images(Arrays.asList("image3.jpg"))
          .build()
    );

    ApiResponse<List<PostResponse>> apiResponse = ApiResponse.<List<PostResponse>>builder().data(response).message("Search post successfully").build();
    when(postService.searchPost(any(), any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(post("/api/posts/searchPost")
          .header("Authorization", "Bearer mocked_jwt_token")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request)))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Search post successfully"));
  }
}
