package com.social_luvina.social_dev8.helpers;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.rmi.ServerException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.social_luvina.social_dev8.modules.services.impl.JwtService;
import com.social_luvina.social_dev8.modules.services.impl.UserService;
import com.social_luvina.social_dev8.modules.services.impl.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
  
  private final JwtService jwtService;
  private final UserService userService;

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

  @Override
  public void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userId;

    if(authHeader == null || !authHeader.startsWith("Bearer ")){

      logger.error("Error: token missing");

      filterChain.doFilter(request, response);
      return;
    }

    jwt = authHeader.substring(7);
    userId = jwtService.getUserIdFromJwt(jwt);
    if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
      // UserDetails userDetails = 

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(jwt, userId);

      authToken.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request)
      );

      SecurityContextHolder.getContext().setAuthentication(authToken);
      logger.info("Xac thuc tai khoan thanh cong");
      
    }

    filterChain.doFilter(request, response);
  }
}
