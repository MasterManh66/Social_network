package com.social_luvina.social_dev8.modules.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {
  @NotBlank(message = "Password mới không được để trống")
  @Size(min = 6, message = "Mật khẩu mới phải có ít nhất 6 ký tự")
  private String newPassword;
}
