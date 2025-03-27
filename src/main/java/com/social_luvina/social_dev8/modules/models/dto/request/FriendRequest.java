package com.social_luvina.social_dev8.modules.models.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendRequest {
  @NotNull(message = "Receiver ID không được để trống")
  @Min(value = 1, message = "Receiver ID phải là số nguyên dương")
  private long receiverId;
}
