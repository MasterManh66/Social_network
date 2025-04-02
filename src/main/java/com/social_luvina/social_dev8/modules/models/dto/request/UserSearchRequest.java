package com.social_luvina.social_dev8.modules.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSearchRequest {
  @NotBlank(message = "Không trống tên người dùng muốn tìm kiếm")
  private String keyword;
}
