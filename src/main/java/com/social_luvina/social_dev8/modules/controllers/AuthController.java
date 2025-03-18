package com.social_luvina.social_dev8.modules.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social_luvina.social_dev8.modules.models.dto.request.ForgetPasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.LoginResponse;
// import com.social_luvina.social_dev8.modules.models.dto.response.LoginResponse;
import com.social_luvina.social_dev8.modules.services.interfaces.UserServiceInterface;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// import org.springframework.web.bind.annotation.GetMapping;
// import com.social_luvina.social_dev8.modules.services.impl.JwtService;

@RestController
@RequestMapping("social/auth")
public class AuthController {

  private final UserServiceInterface userService;

  // @Autowired
  // private JwtService jwtService;
  
  //constructor
  public AuthController(
    UserServiceInterface userService
  ) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){

    Object result = userService.authenticate(request);

    if(result instanceof LoginResponse loginResponse){
      return ResponseEntity.ok(loginResponse);
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error!");
  }


  // @GetMapping("/logout")
  // public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearerToken ) {
  //   try {
      
  //     String token = bearerToken.substring(7);

      

  //   } catch (Exception e) {
  //     return ResponseEntity.internalServerError().body("Network Error!");
  //   }
  // }

  // @PostMapping("/refresh")
  // public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
      
  //     return entity;
  // }
  
  

  @PostMapping("/register")
  public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
    return userService.registerUser(request);
  }  

  @PostMapping("/forgetpassword")
  public ResponseEntity<ApiResponse> forgetPassword(@Valid @RequestBody ForgetPasswordRequest request) {
    return userService.forgetPassword(request);
  }

  @PutMapping("/change_password")
  public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ForgetPasswordRequest request) {
      return userService.changePassword(request);
  }
}
