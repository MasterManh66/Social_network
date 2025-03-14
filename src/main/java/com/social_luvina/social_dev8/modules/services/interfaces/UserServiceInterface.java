package com.social_luvina.social_dev8.modules.services.interfaces;

import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
// import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;


public interface UserServiceInterface {
  
  Object authenticate(LoginRequest request);
  // Object registerUser(RegisterRequest request);

}
