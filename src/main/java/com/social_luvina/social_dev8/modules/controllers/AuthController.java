package com.social_luvina.social_dev8.modules.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
// import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.LoginResponse;
import com.social_luvina.social_dev8.modules.services.interfaces.UserServiceInterface;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("social/auth")
public class AuthController {

  private final UserServiceInterface userService;
  
  //constructor
  public AuthController(
    UserServiceInterface userService
  ) {
    this.userService = userService;
  }

  @PostMapping("login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){

    Object result = userService.authenticate(request);

    if(result instanceof LoginResponse loginResponse){
      return ResponseEntity.ok(loginResponse);
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error!");
  }

  // @PostMapping("register")
  // public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
  //     try {
  //         Object result = userService.registerUser(request);
  //         return ResponseEntity.ok(result);
  //     } catch (Exception e) {
  //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
  //     }
  // }
  
  
}
