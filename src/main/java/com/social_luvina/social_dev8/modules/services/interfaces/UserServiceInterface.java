package com.social_luvina.social_dev8.modules.services.interfaces;

import org.springframework.http.ResponseEntity;

import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;


public interface UserServiceInterface {
  
  Object authenticate(LoginRequest request);
  ResponseEntity<ApiResponse> registerUser(RegisterRequest request);

}
