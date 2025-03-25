package com.social_luvina.social_dev8.modules.services.interfaces;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.multipart.MultipartFile;

import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.UserRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.ForgetPasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.ForgetPasswordResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserResponse;


public interface UserServiceInterface {
  
  Object authenticate(LoginRequest request);
  ResponseEntity<ApiResponse<Void>> registerUser(RegisterRequest request);
  ResponseEntity<ApiResponse<ForgetPasswordResponse>> forgetPassword(ForgetPasswordRequest request);
  ResponseEntity<ApiResponse<Void>> changePassword(ForgetPasswordRequest request);
  ResponseEntity<ApiResponse<UserResponse>> updateProfile(UserRequest request, String token);
  ResponseEntity<InputStreamResource> exportUserReport(String email) throws IOException;
}
