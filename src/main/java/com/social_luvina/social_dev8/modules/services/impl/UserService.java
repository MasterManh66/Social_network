package com.social_luvina.social_dev8.modules.services.impl;
import com.social_luvina.social_dev8.modules.exception.CustomException;
import com.social_luvina.social_dev8.modules.models.dto.request.AuthRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.ChangePasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.ForgetPasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.UserRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.AuthResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.ForgetPasswordResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.LoginResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserDTO;
import com.social_luvina.social_dev8.modules.models.dto.response.UserResponse;
import com.social_luvina.social_dev8.modules.models.entities.Otp;
import com.social_luvina.social_dev8.modules.models.entities.Role;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.models.enums.FriendStatus;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
import com.social_luvina.social_dev8.modules.repositories.CommentRepository;
import com.social_luvina.social_dev8.modules.repositories.FriendRepository;
import com.social_luvina.social_dev8.modules.repositories.LikeRepository;
import com.social_luvina.social_dev8.modules.repositories.OtpRepository;
import com.social_luvina.social_dev8.modules.repositories.PostRepository;
import com.social_luvina.social_dev8.modules.repositories.RoleRepository;
import com.social_luvina.social_dev8.modules.services.interfaces.UserServiceInterface;
import com.social_luvina.social_dev8.modules.utils.ConvertStringToDate;
import com.social_luvina.social_dev8.modules.utils.ExcelGenerator;
import com.social_luvina.social_dev8.modules.utils.GetOtp;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

import org.apache.coyote.BadRequestException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
  
  // private static final Logger logger = LoggerFactory.getLogger(UserService.class);
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PostRepository postRepository;
  private final FriendRepository friendRepository;
  private final CommentRepository commentRepository;
  private final LikeRepository likeRepository;
  private final OtpRepository otpRepository;
  // private final ImageService imageService;

  @Override
  public ResponseEntity<ApiResponse<?>> authenticate(LoginRequest request){

    User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException("Email không chính xác!"));

    if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new BadCredentialsException("Password không chính xác!");
    }

    if (!user.isActive()) {
        otpRepository.deleteAllOtpsByUser(user);
        String otp = GetOtp.generateOtp(6);
        Otp otpCode = Otp.builder().user(user).otpCode(otp).createdAt(LocalDateTime.now()).expiresAt(LocalDateTime.now().plusMinutes(5)).isUsed(false).build();
        otpRepository.save(otpCode);

        UserDTO userDto = new UserDTO(user.getId(),user.getEmail());

        return ResponseEntity.ok(
            ApiResponse.<LoginResponse>builder()
                  .status(HttpStatus.OK.value())
                  .message("Vui lòng nhập OTP để kích hoạt tài khoản.")
                  .data(new LoginResponse(otp, userDto))
                  .build()
        );
    }
    String token = jwtService.generateToken(user.getId(), user.getEmail());

    return ResponseEntity.ok(
        ApiResponse.<AuthResponse>builder()
              .status(HttpStatus.OK.value())
              .message("Đăng nhập thành công!")
              .data(new AuthResponse(token, new UserDTO(user.getId(), user.getEmail())))
              .build()
    );
  } 

  @Override
  public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(AuthRequest request) { 
    try {
      Otp otpRecord = otpRepository.findByOtpCodeAndIsUsedFalse(request.getOtp())
          .orElseThrow(() -> new BadRequestException("OTP không hợp lệ hoặc đã sử dụng!"));

      if (otpRecord.getExpiresAt().isBefore(LocalDateTime.now())) {
          throw new BadRequestException("OTP đã hết hạn!");
      }

      if (!otpRecord.getOtpCode().equals(request.getOtp())) {
        throw new BadRequestException("OTP không chính xác!");
      }

      otpRecord.setUsed(true);
      otpRepository.save(otpRecord);

      User user = otpRecord.getUser();
      user.setActive(true);
      userRepository.save(user);

      otpRepository.deleteAllOtpsByUser(user);

      String token = jwtService.generateToken(user.getId(), user.getEmail());

      return ResponseEntity.ok(
          ApiResponse.<AuthResponse>builder()
              .status(HttpStatus.OK.value())
              .message("Xác thực OTP thành công!")
              .data(new AuthResponse(token, new UserDTO(user.getId(), user.getEmail())))
              .build()
      );
    } catch (BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiResponse.<AuthResponse>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Lỗi xác thực OTP! " + e.getMessage())
                .build()
        );
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
  public ResponseEntity<ApiResponse<Void>> changePassword(ChangePasswordRequest request) { 

    try {
      String emailFromToken = jwtService.extractEmail(request.getToken());
      
      if (emailFromToken == null || !emailFromToken.equals(request.getEmail())) {
        throw new BadCredentialsException("Token không hợp lệ hoặc không khớp với email");
      }

      User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new BadCredentialsException("Email không tồn tại!"));

      user.setPassword(passwordEncoder.encode(request.getNewPassword()));

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

  @Override
  public ResponseEntity<InputStreamResource> exportUserReport(String email) throws IOException {
        // Lấy dữ liệu người dùng
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        // Lấy dữ liệu báo cáo
        LocalDateTime startDate = LocalDateTime.now().minusWeeks(1);
        LocalDateTime endDate = LocalDateTime.now();
        long userId = user.getId();

        int postCount = postRepository.countByUserIdAndCreatedAtBetween(userId, startDate, endDate);
        int friendSenderCount = friendRepository.countByRequesterIdAndFriendStatusAndUpdatedAtBetween(userId, FriendStatus.ACCEPTED, startDate, endDate);
        int friendReceiverCount = friendRepository.countByReceiverIdAndFriendStatusAndUpdatedAtBetween(userId, FriendStatus.ACCEPTED, startDate, endDate);
        int newFriendCount = friendSenderCount + friendReceiverCount;
        int newCommentCount = commentRepository.countByUserIdAndCreatedAtBetween(userId, startDate, endDate);
        int likePostCount = likeRepository.countByUserIdAndCreatedAtBetween(userId, startDate, endDate);
        int totalLike = likePostCount;

        // Tạo file Excel
        ByteArrayInputStream excelFile = ExcelGenerator.generateExcelReport(user, postCount, newFriendCount, totalLike, newCommentCount);

        // Thiết lập header cho file tải về
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=user_report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excelFile));
    }
}