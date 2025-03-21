package com.social_luvina.social_dev8.modules.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ErrorResource;
import com.social_luvina.social_dev8.modules.models.dto.response.LoginResponse;
import com.social_luvina.social_dev8.modules.services.interfaces.UserServiceInterface;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    UserServiceInterface userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){

    Object result = userService.authenticate(request);

    if(result instanceof LoginResponse loginResponse){
      return ResponseEntity.ok(loginResponse);
    }

    if(result instanceof ErrorResource errorResource){ 
      return ResponseEntity.unprocessableEntity().body(errorResource);
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
  
}
