package com.social_luvina.social_dev8.modules.services.impl;

import com.social_luvina.social_dev8.modules.repositories.UserRepository;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.social_luvina.social_dev8.modules.models.entities.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
  
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{ 

    User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new UsernameNotFoundException("User kong hop le"));

    return new org.springframework.security.core.userdetails.User(
      user.getEmail(),
      user.getPassword(),
      Collections.emptyList()
    );
  }
}
