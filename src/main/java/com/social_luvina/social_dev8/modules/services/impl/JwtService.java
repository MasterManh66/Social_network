package com.social_luvina.social_dev8.modules.services.impl;

import java.util.Date;
import java.util.function.Function;
import java.security.Key;
import java.util.Base64;

import com.social_luvina.social_dev8.config.JwtConfig;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

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

    public String getUserIdFromJwt(String token){
      Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

      return claims.getSubject();
    }

    public String getEmailFromJwt(String token){ 
      Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
      return claims.get("email", String.class);
    }

    public boolean isTokenFormatValid(String token){ 
      try {
        String[] tokenParts = token.split("\\.");
        return tokenParts.length == 3;
      } catch (SignatureException e) {
        return false;
      }
    }

    public boolean isSignatureValid(String token){ 
      try {
        
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);

        return true;

      } catch (SignatureException e) {
        return false;
      }
    }

    public Key getSigningKey(){ 
      byte[] keyBytes = jwtConfig.getSecretKey().getBytes();
      return Keys.hmacShaKeyFor(Base64.getEncoder().encode(keyBytes));
    }

    public boolean isTokenExpired(String token){ 

      try {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.after(new Date());
      } catch (SignatureException e) {
        return false;
      }
    }

    public Claims getAllClaimsFromToken(String token){ 
      return Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) { 
      final Claims claims = getAllClaimsFromToken(token);
      return claimsResolver.apply(claims);
    }

    public String extractEmail(String token) {
      return getClaimFromToken(token, claims -> claims.get("email", String.class));
    }

}
