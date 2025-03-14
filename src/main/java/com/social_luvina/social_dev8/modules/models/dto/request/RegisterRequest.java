package com.social_luvina.social_dev8.modules.models.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
  @NotBlank(message = "Email is not empty")
  @Email(message = "Email is not in the correct format")
  @Size(max = 30, message = "Email must not exceed 30 characters")
  private String email;

  @NotBlank(message = "Password is not empty")
  // @Pattern(
  //   regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
  //   message = "Password must contain at least 8 characters, including one uppercase letter, one lowercase letter, one number, and one special character (@$!%*?&).")
  // @Size(max = 20, message = "Password must not exceed 20 characters")
  private String password;

  public String getEmail(){
    return email;
  }

  public String password(){
    return password;
  }
}
