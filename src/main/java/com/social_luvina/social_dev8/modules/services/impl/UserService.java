package com.social_luvina.social_dev8.modules.services.impl;
import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.LoginResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserDTO;
import com.social_luvina.social_dev8.modules.models.entities.Role;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
import com.social_luvina.social_dev8.modules.repositories.RoleRepository;
import com.social_luvina.social_dev8.modules.services.interfaces.UserServiceInterface;

import lombok.RequiredArgsConstructor;
import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
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

  @Override
  public Object authenticate(LoginRequest request){
    
    try {

      User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException("Email or Password wrong!"));

      if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new BadCredentialsException("Email or Password wrong!");
      }

      // if (!request.getPassword().equals(user.getPassword())) {
      //   throw new BadCredentialsException("Email or Password wrong!");
      // }

      UserDTO userDto = new UserDTO(user.getId(),user.getEmail(),user.getPassword());
      String token = jwtService.generateToken(user.getId(), user.getEmail());

      return new LoginResponse(token, userDto);

    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Something wrong!");

    }
  } 

  @Override
  public ResponseEntity<ApiResponse> registerUser(RegisterRequest request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().body(
        ApiResponse.builder()
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
      System.out.println(e.getMessage());
      throw e;
    }

    userRepository.save(newUser);

    return ResponseEntity.ok(
        ApiResponse.builder()
            .status(HttpStatus.OK.value())
            .message("Đăng ký thành công!")
            .data(newUser)
            .build()
    );
}

}