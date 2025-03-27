package com.social_luvina.social_dev8.modules.models.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
  @NotBlank(message = "OTP không được để trống")
  @Pattern(regexp = "^[0-9]{6}$", message = "OTP là số và có 6 ký tự")
  private String otp;
}
