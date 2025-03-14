package com.social_luvina.social_dev8.modules.services.impl;

import java.util.Date;
import java.util.function.Function;
import java.security.Key;
import java.util.Base64;

import com.social_luvina.social_dev8.config.JwtConfig;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
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

    // public String extractUsername(String token){
    //   return extractClaim(token, Claims::getSubject);
    // }

    // private <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
    //   final Claims claims = extractAllClaims(token);
    //   return claimsResolver.apply(claims);
    // }

    // private Claims extractAllClaims(String token) {
    //   return Jwts.parserBuilder()
    //           .setSigningKey(key)
    //           .build()
    //           .parseClaimsJws(token)
    //           .getBody();
    // }

    public String getUserIdFromJwt(String token){
      Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

      return claims.getSubject();
    }

    /*
      1. Token có dung dang kong
      2. Chữ ký của token có dung kong
      3. check xem token đã hết hạn
      4. user_id của token với cái userDetails
      5. check xem token có page blacklist hay kong
      6. check role
     */

    public boolean isValidToken(String token, UserDetails userDetails) {

      try {

        //1 check dang
        if(!isTokenFormatValid(token)){ 
          return false;
        }

        //2
        if(!isSignatureValid(token)){ 
          return false;
        }

        //3
        if(!isTokenExpired(token)){ 
          return false;
        }
        
      } catch (Exception e) {
        return false;
      }

      return false;
    }

    private boolean isTokenFormatValid(String token){ 
      try {
        String[] tokenParts = token.split("\\.");
        return tokenParts.length == 3;
      } catch (Exception e) {
        return false;
      }
    }

    private boolean isSignatureValid(String token){ 
      try {
        
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);

        return true;
      } catch (Exception e) {
        return false;
      }
    }

    private Key getSigningKey(){ 
      byte[] keyBytes = jwtConfig.getSecretKey().getBytes();
      return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token){ 
      final Date expiration = getClaimFromToken(token, Claims::getExpiration);
      return expiration.before(new Date());
    }

    private Claims getAllClaimsFromToken(String token){ 
      return Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) { 
      final Claims claims = getAllClaimsFromToken(token);
      return claimsResolver.apply(claims);
    }
}
