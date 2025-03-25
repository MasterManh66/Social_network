package com.social_luvina.social_dev8.modules.services.impl;

import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.models.dto.response.ErrorResource;
import com.social_luvina.social_dev8.modules.models.dto.response.LoginResponse;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
import com.social_luvina.social_dev8.modules.repositories.RoleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock 
    private UserRepository userRepository;
    @Mock 
    private RoleRepository roleRepository;
    @Mock 
    private PasswordEncoder passwordEncoder;
    @Mock 
    private JwtService jwtService;

    @InjectMocks 
    private UserService userService; 

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).email("user@gmail.com").password("hashed_123456").build();
    }

    @Test
    void authenticate_Success() {
      when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
      when(passwordEncoder.matches("123456", "hashed_123456")).thenReturn(true);
      when(jwtService.generateToken(user.getId(), user.getEmail())).thenReturn("mocked_jwt_token");

      LoginRequest request = new LoginRequest("user@gmail.com", "123456");
      Object response = userService.authenticate(request);

      assertTrue(response instanceof LoginResponse);
      LoginResponse loginResponse = (LoginResponse) response;

      assertEquals("mocked_jwt_token", loginResponse.getToken());
      assertEquals(user.getEmail(), loginResponse.getUser().getEmail());
    }

    @Test
    void authenticate_Fail_EmailNotFound() {
      when(userRepository.findByEmail("wrong@gmail.com")).thenReturn(Optional.empty());

      LoginRequest request = new LoginRequest("wrong@gmail.com", "123456");
      Object response = userService.authenticate(request);

      assertTrue(response instanceof ErrorResource);
      ErrorResource error = (ErrorResource) response;
      assertEquals("Email không chính xác!", error.getErrors().get("message"));
    }


    @Test
    void authenticate_Fail_WrongPassword() {
      when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
      when(passwordEncoder.matches("123456789", "hashed_123456")).thenReturn(false);

      LoginRequest request = new LoginRequest("user@gmail.com", "123456789");
      Object response = userService.authenticate(request);

      assertTrue(response instanceof ErrorResource);
      ErrorResource error = (ErrorResource) response;
      assertEquals("Password không chính xác!", error.getErrors().get("message"));
    }
}
