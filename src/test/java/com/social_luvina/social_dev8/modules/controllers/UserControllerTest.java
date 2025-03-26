package com.social_luvina.social_dev8.modules.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social_luvina.social_dev8.modules.models.dto.request.ChangePasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.ForgetPasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.UserRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.ForgetPasswordResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserResponse;
import com.social_luvina.social_dev8.modules.models.enums.GenderEnum;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
import com.social_luvina.social_dev8.modules.services.interfaces.UserServiceInterface;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
  @InjectMocks
  private UserController userController;

  @Mock
  private UserServiceInterface userService;

  @Mock
  private Authentication authentication;

  @Mock
  private UserRepository userRepository;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  void testRegister_Success() throws Exception {
    RegisterRequest request = RegisterRequest.builder().email("user@gmail.com").password("123456").build();

    ApiResponse<Void> apiResponse = ApiResponse.<Void>builder().message("Register completed").build();

    when(userService.registerUser(any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(post("/social/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Register completed"));
  }

  @Test
  void testForgetPassword_Success() throws Exception {
    String token = "mocked_jwt_token";
    ForgetPasswordRequest request = ForgetPasswordRequest.builder().email("user@gmail.com").build();
    ForgetPasswordResponse response = ForgetPasswordResponse.builder().resetLink("http://localhost:8080/social/auth/change_password?token=" + token).token("mocked_jwt_token").build();

    ApiResponse<ForgetPasswordResponse> apiResponse = ApiResponse.<ForgetPasswordResponse>builder().message("Password reset link generated successfully").data(response).build();

    when(userService.forgetPassword(any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(post("/social/auth/forgetpassword")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Password reset link generated successfully"));
  }

  @Test
  void testChangePassword_Success() throws Exception {
    ChangePasswordRequest request = ChangePasswordRequest.builder().email("user@gmail.com").token("mocked_jwt_token").newPassword("123456789").build();
    ApiResponse<Void> apiResponse = ApiResponse.<Void>builder().message("Password change successfully").build();

    when(userService.changePassword(any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(put("/social/auth/change_password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Password change successfully"));
  }

  @Test
  void testUpdateProject_Success() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date utilDate  = sdf.parse("06/04/2002");
    java.sql.Date dob = new java.sql.Date(utilDate .getTime());

    UserRequest request = UserRequest.builder().firstName("manh").lastName("manh").address("ha noi")
              .gender(GenderEnum.MALE).dateOfBirth("06/04/2002").job("dev").avatar("manhtran.jgp").build();
    UserResponse response = UserResponse.builder().firstName("manh").lastName("manh").address("ha noi")
              .gender(GenderEnum.MALE).dateOfBirth(dob).job("dev").avatar("manhtran.jgp").build();

    ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder().data(response).build();

    when(userService.updateProfile(any(),any())).thenReturn(ResponseEntity.ok(apiResponse));

    mockMvc.perform(put("/social/auth/profile")
        .header("Authorization", "Bearer mocked_jwt_token")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void testDownloadReport_Success() throws Exception {
    String email = "user@gmail.com";
    when(userService.exportUserReport(email)).thenReturn(ResponseEntity.ok().build());

    mockMvc.perform(get("/social/auth/report")
          .param("email", email)
          .header("Authorization", "Bearer mocked_jwt_token")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
  }
}
