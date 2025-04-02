package com.social_luvina.social_dev8.modules.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ForgetPasswordOtpResponse {
  private final String otp;
}
