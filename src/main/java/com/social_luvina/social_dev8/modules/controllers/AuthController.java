package com.social_luvina.social_dev8.modules.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
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
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
    return ResponseEntity.ok(userService.authenticate(request));
  }
}
