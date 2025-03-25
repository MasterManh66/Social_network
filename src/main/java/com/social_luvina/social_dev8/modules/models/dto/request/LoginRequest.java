package com.social_luvina.social_dev8.modules.models.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Valid
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
  
  @NotBlank(message = "Email is not empty")
  @Email(message = "Email is not in the correct format")
  private String email;

  @NotBlank(message = "Password is not empty")
  private String password;
}
