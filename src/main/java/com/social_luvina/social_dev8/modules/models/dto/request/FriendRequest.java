package com.social_luvina.social_dev8.modules.models.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendRequest {
  @NotNull(message = "Receiver ID is required")
  private long receiverId;
}
