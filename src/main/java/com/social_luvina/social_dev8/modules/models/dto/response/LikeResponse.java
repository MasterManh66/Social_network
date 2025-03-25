package com.social_luvina.social_dev8.modules.models.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeResponse {
  private long id;
  private LocalDateTime createdAt;
  private long postId;
  private long userId;
}
