package com.social_luvina.social_dev8.modules.controllers;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    
    // SuccessResource<UserDTO> response = new SuccessResource<>("SUCCESS", userDto);
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
  public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ForgetPasswordRequest request) {
      return userService.changePassword(request);
  }

  @PutMapping("/profile")
  public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
    @Valid @RequestBody UserRequest request, @RequestHeader("Authorization") String token){ 

    ResponseEntity<ApiResponse<UserResponse>> response = userService.updateProfile(request, token);
    return response;
  }
}
