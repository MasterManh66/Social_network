package com.social_luvina.social_dev8.modules.models.dto.response;

import com.social_luvina.social_dev8.modules.models.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
  private long id;
  private long userId;
  private String title;
  private String content;
  private LocalDateTime createdAt;
  private PostStatus postStatus;
  private List<String> images;
}
