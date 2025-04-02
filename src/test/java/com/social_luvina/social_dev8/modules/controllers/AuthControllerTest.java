package com.social_luvina.social_dev8.modules.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social_luvina.social_dev8.modules.models.dto.request.AuthRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.AuthResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.LoginResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserDTO;
import com.social_luvina.social_dev8.modules.services.interfaces.UserServiceInterface;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

  @InjectMocks
  private AuthController authController;

  @Mock
  private UserServiceInterface userService;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
  }

  @Test
  void testLogin_Success_ReturnsLoginResponse() throws Exception {
    // UserDTO userDto = new UserDTO(1L, "manhtran@gmail.com"); 
    LoginRequest request = new LoginRequest("user@gmail.com", "123456");
    LoginResponse loginResponse = new LoginResponse("643215");

    ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder().message("Vui lòng nhập OTP để kích hoạt tài khoản.").data(loginResponse).build();

    when(userService.authenticate(any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(post("/social/auth/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
  }

  @Test
  void testLogin_Success_ReturnsAuthResponse() throws Exception {
    UserDTO userDto = new UserDTO(1L, "manhtran@gmail.com"); 
    LoginRequest request = new LoginRequest("user@gmail.com", "123456");
    AuthResponse authResponse = new AuthResponse("mocked_jwt_token", userDto);

    ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder().message("Đăng nhập thành công!").data(authResponse).build();

    when(userService.authenticate(any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(post("/social/auth/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
  }

  @Test
  void testVerifyOtp_Success() throws Exception {
    UserDTO userDto = new UserDTO(1L, "manhtran@gmail.com"); 
    AuthRequest request = new AuthRequest("643215");
    AuthResponse authResponse = new AuthResponse("mocked_jwt_token", userDto);

    ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder().message("Xác thực OTP thành công!").data(authResponse).build();

    when(userService.verifyOtp(any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(post("/social/auth/verify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
  }
}

