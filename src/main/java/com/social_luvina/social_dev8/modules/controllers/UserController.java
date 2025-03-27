package com.social_luvina.social_dev8.modules.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social_luvina.social_dev8.modules.models.dto.request.ChangePasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.ForgetPasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.UserRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.ForgetPasswordResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserDTO;
import com.social_luvina.social_dev8.modules.models.dto.response.UserResponse;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
import com.social_luvina.social_dev8.modules.services.interfaces.UserServiceInterface;

import jakarta.validation.Valid;
import java.io.IOException;

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

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
    return userService.registerUser(request);
  }  

  @PostMapping("/forgetpassword")
  public ResponseEntity<ApiResponse<ForgetPasswordResponse>> forgetPassword(@Valid @RequestBody ForgetPasswordRequest request) {
    return userService.forgetPassword(request);
  }

  @PutMapping("/change_password")
  public ResponseEntity<ApiResponse<Void>> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest request) {
      return userService.changePassword(authentication, request);
  }

  @PutMapping("/profile")
  public ResponseEntity<ApiResponse<UserResponse>> updateProfile(Authentication authentication, @Valid @RequestBody UserRequest request){ 
    return userService.updateProfile(authentication, request);
  }

  @GetMapping("/report")
  public ResponseEntity<InputStreamResource> downloadReport(Authentication authentication) throws IOException {
    return userService.exportUserReport(authentication);
  }
}
