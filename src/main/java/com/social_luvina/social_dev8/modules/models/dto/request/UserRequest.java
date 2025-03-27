package com.social_luvina.social_dev8.modules.models.dto.request;

import com.social_luvina.social_dev8.modules.models.enums.GenderEnum;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
  @Size(max = 20, message = "First name không vượt quá 20 ký tự")
  private String firstName;

  @Size(max = 20, message = "Last name không vượt quá 20 ký tự")
  private String lastName;

  @Size(max = 255, message = "Address không vượt quá 255 ký tự")
  private String address;

  private GenderEnum gender;

  @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(19|20)\\d{2}$",message = "Date không đúng định dạng dd/MM/yyyy")
  private String dateOfBirth;

  @Size(max = 100, message = "Job không vượt quá 100 ký tự")
  private String job;

  private String avatar;
}
