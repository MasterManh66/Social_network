package com.social_luvina.social_dev8.modules.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social_luvina.social_dev8.modules.models.dto.response.UserDTO;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("social/auth")
public class UserController {
  
  @Autowired
  private UserRepository userRepository;

  @GetMapping("/me")
  public ResponseEntity<?> me(){
    String email = "test@gmail.com";

    User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User does not exist!"));

    UserDTO userDto = new UserDTO(user.getId(), user.getEmail(), user.getPassword());

    return ResponseEntity.ok(userDto);
  }
}
