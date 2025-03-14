package com.social_luvina.social_dev8.modules.services.impl;

import java.util.Date;
import java.security.Key;
import java.util.Base64;

import com.social_luvina.social_dev8.config.JwtConfig;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final JwtConfig jwtConfig;
    private final Key key;

    public JwtService(
      JwtConfig jwtConfig)
    {
      this.jwtConfig = jwtConfig;
      this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes()));
    }

    public String generateToken(Long userId, String email){
      Date now = new Date();
      Date expiryDate = new Date(now.getTime() + jwtConfig.getExpirationTime());

      return Jwts.builder()
          .setSubject(String.valueOf(userId))
          .claim("email", email)
          .setIssuedAt(now)
          .setExpiration(expiryDate)
          .signWith(key, SignatureAlgorithm.HS256)
          .compact();
    }
}
