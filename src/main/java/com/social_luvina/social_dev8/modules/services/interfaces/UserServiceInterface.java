package com.social_luvina.social_dev8.modules.services.interfaces;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.UserRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.AuthRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.ChangePasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.ForgetPasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.AuthResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.ForgetPasswordResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserResponse;


public interface UserServiceInterface {
  
  ResponseEntity<ApiResponse<?>> authenticate(LoginRequest request);
  ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(AuthRequest request) ;
  ResponseEntity<ApiResponse<Void>> registerUser(RegisterRequest request);
  ResponseEntity<ApiResponse<ForgetPasswordResponse>> forgetPassword(ForgetPasswordRequest request);
  ResponseEntity<ApiResponse<Void>> changePassword(ChangePasswordRequest request);
  ResponseEntity<ApiResponse<UserResponse>> updateProfile(UserRequest request, String token);
  ResponseEntity<InputStreamResource> exportUserReport(String email) throws IOException;
}
