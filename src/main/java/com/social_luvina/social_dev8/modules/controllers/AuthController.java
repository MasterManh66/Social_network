package com.social_luvina.social_dev8.modules.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social_luvina.social_dev8.modules.models.dto.request.AuthRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.AuthResponse;
import com.social_luvina.social_dev8.modules.services.interfaces.UserServiceInterface;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("social/auth")
public class AuthController {

  private final UserServiceInterface userService;

  public AuthController(
    UserServiceInterface userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest request){
    return userService.authenticate(request);
  }

  @PostMapping("/verify")
  public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(@Valid @RequestBody AuthRequest request) {
        return userService.verifyOtp(request);
  }
  
}
