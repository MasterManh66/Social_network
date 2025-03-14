package com.social_luvina.social_dev8.modules.services.impl;
import com.social_luvina.social_dev8.modules.models.dto.request.LoginRequest;
// import com.social_luvina.social_dev8.modules.models.dto.request.RegisterRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.LoginResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.UserDTO;
import com.social_luvina.social_dev8.modules.models.entities.User;
// import com.social_luvina.social_dev8.modules.models.entities.Role;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
// import com.social_luvina.social_dev8.modules.repositories.RoleRepository;
import com.social_luvina.social_dev8.modules.services.interfaces.UserServiceInterface;

// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserServiceInterface {
  
  // private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private JwtService jwtService;

  // @Autowired
  // private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  // @Autowired
  // private RoleRepository roleRepository;

  @Override
  public Object authenticate(LoginRequest request){
    
    try {

      User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException("Email or Password wrong!"));

      // if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      //   throw new BadCredentialsException("Email or Password wrong!");
      // }

      if (!request.getPassword().equals(user.getPassword())) {
        throw new BadCredentialsException("Email or Password wrong!");
    }

      UserDTO userDto = new UserDTO(user.getId(),user.getEmail(),user.getPassword());
      String token = jwtService.generateToken(user.getId(), user.getEmail());

      return new LoginResponse(token, userDto);

    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Something wrong!");

    }
  } 

  // @Override 
  // public Object registerUser(RegisterRequest request){
  //   if (userRepository.findByEmail(request.getEmail()).isPresent()) {
  //     throw new RuntimeException("Email already registered!");
  //   }

  //   Role userRole = roleRepository.findByRoleName("User").orElseThrow(() -> new RuntimeException("Role USER not found!"));
  //   List<Role> role = new ArrayList<>(Collections.singletonList(userRole));

  //   User newUser = User.builder()
  //       .email(request.getEmail())
  //       .password(request.getPassword())
  //       .roles(role)
  //       .build();

  //   userRepository.save(newUser);

  //   return "User registered successfully!";
  // }

}