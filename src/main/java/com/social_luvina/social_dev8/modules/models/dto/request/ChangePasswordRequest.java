package com.social_luvina.social_dev8.modules.models.dto.request;

import jakarta.validation.constraints.Email;
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
  @NotBlank(message = "Email is not empty")
  @Email(message = "Email is not in the correct format")
  private String email;

  private String token;

  @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
  private String newPassword;
}
