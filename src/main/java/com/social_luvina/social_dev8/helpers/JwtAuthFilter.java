package com.social_luvina.social_dev8.helpers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import com.social_luvina.social_dev8.modules.services.impl.CustomUserDetailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.social_luvina.social_dev8.modules.services.impl.JwtService;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
  
  private final JwtService jwtService;
  private final CustomUserDetailService customUserDetailService;
  private final ObjectMapper objectMapper;

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

  @Override
  protected boolean shouldNotFilter(
    @NonNull HttpServletRequest request
  ){ 
    String path = request.getRequestURI();
    return path.startsWith("/social/auth/login");
  }

  @Override
  public void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    try {
      final String authHeader = request.getHeader("Authorization");
      final String jwt;
      final String userId;

      if(authHeader == null || !authHeader.startsWith("Bearer ")){
        sendErrorResponse(response,request,
          HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "Xác thực không thành công",
          "Không tìm thấy token"
        );

        // logger.error("Error: token missing");

        // filterChain.doFilter(request, response);
        return;
      }

      jwt = authHeader.substring(7);

      if(!jwtService.isTokenFormatValid(jwt)){ 
        sendErrorResponse(response,request,
          HttpServletResponse.SC_UNAUTHORIZED,
          "Xác thực không thành công",
          "Token không đúng định dạng"
        );
        return;
      }

      if(!jwtService.isSignatureValid(jwt)){ 
        sendErrorResponse(response,request,
          HttpServletResponse.SC_UNAUTHORIZED,
          "Xác thực không thành công",
          "Chữ ký không hợp lệ"
        );
        return;
      }

      if(!jwtService.isTokenExpired(jwt)){ 
        sendErrorResponse(response,request,
          HttpServletResponse.SC_UNAUTHORIZED,
          "Xác thực không thành công",
          "Token đã hết hạn"
        );
        return;
      }

      userId = jwtService.getUserIdFromJwt(jwt);
      if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
        UserDetails userDetails = customUserDetailService.loadUserByUsername(userId);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );

        authToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.info("Account verification successful !!!" + userDetails.getUsername());
        
      }

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  private void sendErrorResponse(
    @NotNull HttpServletResponse response,
    @NotNull HttpServletRequest request,
    int statusCode,
    String error,
    String message
  ) throws IOException { 

    response.setStatus(statusCode);
    response.setContentType("application/json;charset=UTF-8");

    Map<String, Object> errorResponse = new HashMap<>();

    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", statusCode);
    errorResponse.put("error", error);
    errorResponse.put("message", message);
    errorResponse.put("path", request.getRequestURI());

    String jsonResponse = objectMapper.writeValueAsString(errorResponse);

    response.getWriter().write(jsonResponse);
  }
}
