package com.social_luvina.social_dev8.modules.models.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgetPasswordRequest {
  @NotBlank(message = "Email không được để trống")
  @Email(message = "Email đang không đúng định dạng!")
  private String email;
}
