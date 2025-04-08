package com.social_luvina.social_dev8.modules.services.impl;
import com.social_luvina.social_dev8.modules.exception.CustomException;
import com.social_luvina.social_dev8.modules.models.dto.request.AuthRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.ChangePasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.ForgetPasswordOtpRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.ForgetPasswordRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.UserRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.UserSearchRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.AuthResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.ForgetPasswordOtpResponse;
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

import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
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

  private User getAuthenticatedUser(Authentication authentication) {
    return userRepository.findByEmail(authentication.getName())
          .orElseThrow(() -> new BadCredentialsException("User không tồn tại"));
  }

  @Override
  public ResponseEntity<ApiResponse<LoginResponse>> authenticate(LoginRequest request){

    User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
          .orElseThrow(() -> new CustomException("Email không chính xác!", HttpStatus.NOT_FOUND));

    if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new CustomException("Password không chính xác!", HttpStatus.NOT_FOUND);
    }

    otpRepository.deleteAllOtpsByUser(user);
    String otp = GetOtp.generateOtp(6);
    Otp otpCode = Otp.builder().user(user).otpCode(otp).createdAt(LocalDateTime.now()).expiresAt(LocalDateTime.now().plusMinutes(5)).isUsed(false).build();
    otpRepository.save(otpCode);

    return ResponseEntity.ok(
        ApiResponse.<LoginResponse>builder()
                  .status(HttpStatus.OK.value())
                  .message("Vui lòng nhập OTP để xác thực tài khoản.")
                  .data(new LoginResponse(otp))
                  .build()
    );
  } 

  @Override
  public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(AuthRequest request) { 
    Otp otpRecord = otpRepository.findByOtpCodeAndIsUsedFalse(request.getOtp())
        .orElseThrow(() -> new CustomException("OTP không hợp lệ hoặc đã sử dụng!", HttpStatus.BAD_REQUEST));

    if (otpRecord.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new CustomException("OTP đã hết hạn!", HttpStatus.NOT_FOUND);
    }

    if (!otpRecord.getOtpCode().equals(request.getOtp())) {
      throw new CustomException("OTP không chính xác!", HttpStatus.BAD_REQUEST);
    }

    otpRecord.setUsed(true);
    otpRepository.save(otpRecord);

    User user = otpRecord.getUser();
    if (!user.isActive()) {
      user.setActive(true);
      userRepository.save(user);
      throw new CustomException("Tài khoản đã được kích hoạt !", HttpStatus.OK);
    }

    otpRepository.deleteAllOtpsByUser(user);

    String token = jwtService.generateToken(user.getId(), user.getEmail());
    UserDTO userDto = new UserDTO(user.getId(),user.getEmail());

    return ResponseEntity.ok(
        ApiResponse.<AuthResponse>builder()
              .status(HttpStatus.OK.value())
              .message("Xác thực OTP thành công!")
              .data(new AuthResponse(token, userDto))
              .build()
    );
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> registerUser(RegisterRequest request) {
    if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
      throw new CustomException("Email không được để trống!", HttpStatus.BAD_REQUEST);
    }

    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new CustomException("Email đã tồn tại!",  HttpStatus.CONFLICT);
    }

    Role userRole = roleRepository.findByRoleName("User")
            .orElseThrow(() -> new CustomException("Role USER không tồn tại!", HttpStatus.NOT_FOUND));
     
    User newUser = User.builder()
      .email(request.getEmail().toLowerCase().trim())
      .password(passwordEncoder.encode(request.getPassword()))
      .roles(Collections.singletonList(userRole))
      .build();

    userRepository.save(newUser);

    return ResponseEntity.ok(
        ApiResponse.<Void>builder()
            .status(HttpStatus.CREATED.value())
            .message("Đăng ký tài khoản thành công!")
            .build()
    );
  }

  @Override
  public ResponseEntity<ApiResponse<ForgetPasswordOtpResponse>> forgetPassword(ForgetPasswordRequest request) { 
      User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
              .orElseThrow(() -> new CustomException("Email không tồn tại!", HttpStatus.NOT_FOUND));

      otpRepository.deleteAllOtpsByUser(user);
      String otp = GetOtp.generateOtp(6);
      Otp otpCode = Otp.builder().user(user).otpCode(otp).createdAt(LocalDateTime.now()).expiresAt(LocalDateTime.now().plusMinutes(5)).isUsed(false).build();
      otpRepository.save(otpCode);

      return ResponseEntity.ok(
          ApiResponse.<ForgetPasswordOtpResponse>builder()
            .status(HttpStatus.OK.value())
            .message("Quên mật khẩu Thành công! Vui lòng xác thực OTP.")
            .data(new ForgetPasswordOtpResponse(otp))
            .build()
      );
  }

  @Override
  public ResponseEntity<ApiResponse<ForgetPasswordResponse>> verifyForgetPassword(ForgetPasswordOtpRequest request) {
    Otp otpRecord = otpRepository.findByOtpCodeAndIsUsedFalse(request.getOtp())
        .orElseThrow(() -> new CustomException("OTP không hợp lệ hoặc đã sử dụng!", HttpStatus.BAD_REQUEST));

    if (otpRecord.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new CustomException("OTP đã hết hạn!", HttpStatus.NOT_FOUND);
    }

    if (!otpRecord.getOtpCode().equals(request.getOtp())) {
      throw new CustomException("OTP không chính xác!", HttpStatus.BAD_REQUEST);
    }

    otpRecord.setUsed(true);
    otpRepository.save(otpRecord);

    User user = otpRecord.getUser();
    otpRepository.deleteAllOtpsByUser(user);

    String token = jwtService.generateToken(user.getId(), user.getEmail()); 
    String resetLink = "http://localhost:8080/social/auth/change_password?token=" + token;

    return ResponseEntity.ok(
        ApiResponse.<ForgetPasswordResponse>builder()
            .status(HttpStatus.OK.value())
            .message("Xác thực OTP thành công! Mở link dưới và thay đổi mật khẩu.")
            .data(new ForgetPasswordResponse(resetLink,token))
            .build()
    );
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> changePassword(Authentication authentication, ChangePasswordRequest request) { 
    User user = getAuthenticatedUser(authentication);

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));

    userRepository.save(user);

    return ResponseEntity.ok(
        ApiResponse.<Void>builder()
          .status(HttpStatus.OK.value())
          .message("Đổi mật khẩu thành công! Hãy đăng nhập lại.")
          .build()
    );
  }

  @Override
  public ResponseEntity<ApiResponse<UserResponse>> updateProfile(Authentication authentication, UserRequest request){ 
      User user = getAuthenticatedUser(authentication);
      boolean isUpdated = false;

      if (request.getFirstName() != null && !request.getFirstName().equals(user.getFirstName())) {
        user.setFirstName(request.getFirstName());
        isUpdated = true;
      } 
      if (request.getLastName() != null && !request.getLastName().equals(user.getLastName())) {
        user.setLastName(request.getLastName());
        isUpdated = true;
      }
      if (request.getDateOfBirth() != null && (user.getDateOfBirth() == null || !request.getDateOfBirth().equals(user.getDateOfBirth().toString()))) {
        user.setDateOfBirth(ConvertStringToDate.convert(request.getDateOfBirth()));
        isUpdated = true;
      }
      if (request.getAddress() != null && !request.getAddress().equals(user.getAddress())) {
        user.setAddress(request.getAddress());
        isUpdated = true;
      }
      if (request.getJob() != null && !request.getJob().equals(user.getJob())) {
        user.setJob(request.getJob());
        isUpdated = true;
      }
      if (request.getAvatar() != null && !request.getAvatar().equals(user.getAvatar())) {
        user.setAvatar(request.getAvatar());
        isUpdated = true;
      }
      if (request.getGender() != null && !request.getGender().equals(user.getGender())) {
        user.setGender(request.getGender());
        isUpdated = true;
      }    
      if(isUpdated){
        userRepository.save(user);
      }
      
      return ResponseEntity.ok(
          ApiResponse.<UserResponse>builder()
          .status(HttpStatus.CREATED.value())
          .message("Cập nhật thông tin thành công!")
          .data(new UserResponse(user))
          .build()
      );
  }

  @Override
  public ResponseEntity<ApiResponse<UserResponse>> getUserById(Authentication authentication) {
    User user = getAuthenticatedUser(authentication);
    User userById = userRepository.findById(user.getId())
        .orElseThrow(() -> new CustomException("Người dùng không tồn tại!", HttpStatus.NOT_FOUND));
    
    UserResponse response = new UserResponse(userById);
    response.setFullName(userById.getFirstName() + " " + userById.getLastName());

    return ResponseEntity.ok(
        ApiResponse.<UserResponse>builder()
          .status(HttpStatus.OK.value())
          .message("Lấy thông tin người dùng thành công!")
          .data(response)
          .build()
    );
  }

  @Override
  public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsers(Authentication authentication, UserSearchRequest request){ 
    if (request.getKeyword() == null && request.getKeyword().trim().isEmpty()){
      throw new CustomException("Vui lòng nhập tên user muốn tìm kiếm!", HttpStatus.BAD_REQUEST);
    }

    List<User> users = userRepository.searchByKeyword(request.getKeyword().toLowerCase().trim());

    if (users.isEmpty()){
      throw new CustomException("Không tìm thấy người dùng nào!", HttpStatus.NOT_FOUND);
    }
    List<UserResponse> userResponses = users.stream().map(UserResponse::new).toList();
    return ResponseEntity.ok(
        ApiResponse.<List<UserResponse>>builder()
          .status(HttpStatus.OK.value())
          .message("Tìm kiếm người dùng thành công!")
          .data(userResponses)
          .build()
    );
  }

  @Override
  public ResponseEntity<InputStreamResource> exportUserReport(Authentication authentication) throws IOException{
    User user = getAuthenticatedUser(authentication);

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

    ByteArrayInputStream excelFile = ExcelGenerator.generateExcelReport(user, postCount, newFriendCount, totalLike, newCommentCount);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "attachment; filename=user_report.xlsx");

    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(new InputStreamResource(excelFile));
    }
}