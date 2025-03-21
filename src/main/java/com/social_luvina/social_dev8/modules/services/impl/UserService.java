package com.social_luvina.social_dev8.modules.services.impl;
import com.social_luvina.social_dev8.modules.models.dto.request.ForgetPasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
// import com.social_luvina.social_dev8.modules.models.dto.request.UploadImageRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.UserRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.ErrorResource;
import com.social_luvina.social_dev8.modules.models.dto.response.ForgetPasswordResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.LoginResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserDTO;
import com.social_luvina.social_dev8.modules.models.dto.response.UserResponse;
import com.social_luvina.social_dev8.modules.models.entities.Role;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
import com.social_luvina.social_dev8.modules.repositories.RoleRepository;
import com.social_luvina.social_dev8.modules.services.interfaces.UserServiceInterface;
import com.social_luvina.social_dev8.modules.utils.ConvertStringToDate;

import lombok.RequiredArgsConstructor;

// import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
// import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
  
  // private static final Logger logger = LoggerFactory.getLogger(UserService.class);
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  // private final ImageService imageService;

  @Override
  public Object authenticate(LoginRequest request){
    
    try {

      User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException("Email không chính xác!"));

      if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new BadCredentialsException("Password không chính xác!");
      }

      UserDTO userDto = new UserDTO(user.getId(),user.getEmail(),user.getPassword());
      String token = jwtService.generateToken(user.getId(), user.getEmail());

      return new LoginResponse(token, userDto);

    } catch (BadCredentialsException e) {
      // throw new BadCredentialsException("Something wrong!");
      Map<String, String> errors = new HashMap<>();
      errors.put("message", e.getMessage());
      ErrorResource errorResource = new ErrorResource("Có vấn đề xảy ra trong quá trình xác thực", errors);
      return errorResource;
    }
  } 

  @Override
  public ResponseEntity<ApiResponse<Void>> registerUser(RegisterRequest request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().body(
        ApiResponse.<Void>builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message("Email đã tồn tại!")
            .build()
      );
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    Role userRole = roleRepository.findByRoleName("User")
            .orElseThrow(() -> new RuntimeException("Role USER không tồn tại!"));
    User newUser;
    try {
      newUser = User.builder()
        .email(request.getEmail().toLowerCase())
        .password(encodedPassword)
        .roles(Collections.singletonList(userRole))
        .isActive(false)
        .build();
    } catch (Exception e) {
      return ResponseEntity.ok(
        ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message("Đăng ký không thành công thành công!" +e.getMessage())
            .build()
      );
    }

    userRepository.save(newUser);

    return ResponseEntity.ok(
        ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message("Đăng ký tài khoản thành công!")
            // .data(newUser)
            .build()
    );
  }

  @Override
  public ResponseEntity<ApiResponse<ForgetPasswordResponse>> forgetPassword(ForgetPasswordRequest request) { 
    User user = userRepository.findByEmail(request.getEmail())
              .orElseThrow(() -> new BadCredentialsException("Email không tồn tại!"));

      String token = jwtService.generateToken(user.getId(), user.getEmail()); 
      String resetLink = "http://localhost:8080/social/auth/change_password?token=" + token;

      return ResponseEntity.ok(
          ApiResponse.<ForgetPasswordResponse>builder()
          .status(HttpStatus.OK.value())
          .message("Quên mật khẩu Thành công! Mở link dưới và thay đổi mật khẩu.")
          .data(new ForgetPasswordResponse(resetLink,token))
          .build()
      );
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> changePassword(ForgetPasswordRequest request) { 

    try {
      String emailFromToken = jwtService.extractEmail(request.getToken());
      
      if (emailFromToken == null || !emailFromToken.equals(request.getEmail())) {
        throw new BadCredentialsException("Token không hợp lệ hoặc không khớp với email");
      }

      User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new BadCredentialsException("Email không tồn tại!"));

      user.setPassword(passwordEncoder.encode(request.getPassword()));

      userRepository.save(user);

      return ResponseEntity.ok(
          ApiResponse.<Void>builder()
          .status(HttpStatus.OK.value())
          .message("Đổi mật khẩu thành công! Hãy đăng nhập lại.")
          .build()
      );
    } catch (BadCredentialsException e) {
      return ResponseEntity.ok(
        ApiResponse.<Void>builder()
        .status(HttpStatus.UNAUTHORIZED.value())
        .message(e.getMessage())
        .build()
      );
    }
  }

  @Override
  public ResponseEntity<ApiResponse<UserResponse>> updateProfile(UserRequest request, String token){ 
    try {
      String email = jwtService.extractEmail(token.replace("Bearer ", ""));
      User user = userRepository.findByEmail(email)
          .orElseThrow(() -> new RuntimeException("User không tồn tại"));
      
      user.setFirstName(request.getFirstName());
      user.setLastName(request.getLastName());
      user.setDateOfBirth(ConvertStringToDate.convert(request.getDateOfBirth()));
      user.setAddress(request.getAddress());
      user.setJob(request.getJob());
      user.setAvatar(request.getAvatar());
      user.setGender(request.getGender());

      userRepository.save(user);

      return ResponseEntity.ok(
          ApiResponse.<UserResponse>builder()
          .status(HttpStatus.OK.value())
          .message("Cập nhật thông tin thành công!")
          .data(new UserResponse(user))
          .build()
      );

    } catch (Exception e) {
      return ResponseEntity.ok(
            ApiResponse.<UserResponse>builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("Có lỗi xảy ra khi cập nhật hồ sơ: " + e.getMessage())
            .data(null)
            .build()
      );
    }
  }

}