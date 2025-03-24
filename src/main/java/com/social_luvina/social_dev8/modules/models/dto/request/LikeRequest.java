package com.social_luvina.social_dev8.modules.models.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeRequest {
  @NotNull(message = "PostId is required")
  private long postId;
}
