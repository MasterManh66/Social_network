package com.social_luvina.social_dev8.modules.models.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.social_luvina.social_dev8.modules.models.enums.FriendStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendResponse {
  private long userId;
  private String fullName;
  private String avatar;
  private FriendStatus status;
  private LocalDateTime createdAt;
}
