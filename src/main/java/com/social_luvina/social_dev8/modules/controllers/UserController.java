package com.social_luvina.social_dev8.modules.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social_luvina.social_dev8.modules.models.dto.request.ChangePasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.ForgetPasswordOtpRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.ForgetPasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.UserRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.UserSearchRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.ForgetPasswordOtpResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.ForgetPasswordResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserDTO;
import com.social_luvina.social_dev8.modules.models.dto.response.UserResponse;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
import com.social_luvina.social_dev8.modules.services.interfaces.UserServiceInterface;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("social/auth")
public class UserController {
  
  @Autowired
  private UserRepository userRepository;

  private final UserServiceInterface userService;

  public UserController(
    UserServiceInterface userService) {
    this.userService = userService;
  }

  @GetMapping("/me")
  public ResponseEntity<?> me(){
    String email = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User does not exist!"));

    UserDTO userDto = UserDTO.builder()
      .id(user.getId())
      .email(user.getEmail())
      .build();
    return ResponseEntity.ok(userDto);
  }

  @Operation(summary = "Register a new user", description = "Register a new user")
  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
    return userService.registerUser(request);
  }  

  @Operation(summary = "ForgetPassword for user", description = "ForgetPassword for user")
  @PostMapping("/forgetpassword")
  public ResponseEntity<ApiResponse<ForgetPasswordOtpResponse>> forgetPassword(@Valid @RequestBody ForgetPasswordRequest request) {
    return userService.forgetPassword(request);
  }

  @Operation(summary = "OTP authentication forgetPassword", description = "OTP authentication forgetPassword")
  @PostMapping("/verifyForgetpassword")
  public ResponseEntity<ApiResponse<ForgetPasswordResponse>> verifyForgetPassword(@Valid @RequestBody ForgetPasswordOtpRequest request) {
    return userService.verifyForgetPassword(request);
  }

  @Operation(summary = "Change Password for user", description = "Change Password for user")
  @PutMapping("/change_password")
  public ResponseEntity<ApiResponse<Void>> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest request) {
      return userService.changePassword(authentication, request);
  }

  @Operation(summary = "Update profile for user", description = "Update profile for user")
  @PutMapping(value = "/profile")
  public ResponseEntity<ApiResponse<UserResponse>> updateProfile(Authentication authentication, @Valid @RequestBody UserRequest request ) {
    return userService.updateProfile(authentication, request);
  }

  @Operation(summary = "Export report excel for user", description = "Export report excel for user")
  @GetMapping("/report")
  public ResponseEntity<InputStreamResource> downloadReport(Authentication authentication) throws IOException {
    return userService.exportUserReport(authentication);
  }

  @Operation(summary = "Watch info user", description = "Watch info user")
  @GetMapping("/infoUser")
  public ResponseEntity<ApiResponse<UserResponse>> getUserById(Authentication authentication) {
    return userService.getUserById(authentication);
  }

  @Operation(summary = "Search for users", description = "Search for users")
  @PostMapping("/searchUser")
  public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsers(Authentication authentication, @Valid @RequestBody UserSearchRequest request) {
      return userService.searchUsers(authentication, request);
  }
  
}
