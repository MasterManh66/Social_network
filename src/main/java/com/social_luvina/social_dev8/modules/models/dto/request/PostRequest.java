package com.social_luvina.social_dev8.modules.models.dto.request;

import com.social_luvina.social_dev8.modules.models.enums.PostStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {
  @Size(max = 255,message = "Title must not exceed 255 characters")
  private String title;

  @Size(max = 10000,message = "Title must not exceed 2500 words")
  private String content;

  private PostStatus postStatus;

  private List<String> images;
}
