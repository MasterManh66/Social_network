package com.social_luvina.social_dev8.modules.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

// return
@Data
@AllArgsConstructor
public class LoginResponse {

  private final String token;
  private final UserDTO user;


}
