package com.social_luvina.social_dev8.modules.models.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendRequest {
  @NotNull(message = "Receiver ID is required")
  private long receiverId;
}
