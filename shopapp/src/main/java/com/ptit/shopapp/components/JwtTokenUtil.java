package com.ptit.shopapp.components;

import com.ptit.shopapp.exceptions.InvalidParamException;
import com.ptit.shopapp.models.Token;
import com.ptit.shopapp.models.User;
import com.ptit.shopapp.repositories.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
  private final TokenRepository tokenRepository;
  private final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
  @Value("${jwt.expiration}")
  private int expiration; // luu vao bien moi truong

  @Value("${jwt.secretKey}")
  private String secretKey;
  public String generateToken(User user) throws Exception {
    // thuoc tinh => claims
    Map<String, Object> claims = new HashMap<>();
    //this.generateSecretKey();
    claims.put("phoneNumber", user.getPhoneNumber());
    claims.put("userId", user.getId());
    try {
      // trong token co chua claim ten la phoneNumber
      String token = Jwts.builder()
          .setClaims(claims)
          .setSubject(user.getPhoneNumber())
          .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
          .signWith(getSignInKey(), SignatureAlgorithm.HS256)
          .compact();
      return token;
    } catch (Exception e){
      // co the inject logger
      throw new InvalidParamException(e.getMessage());
    }
  }

  private Key getSignInKey(){
    byte[] bytes = Decoders.BASE64.decode(secretKey);
    //Keys.hmacShaKeyFor(Decoders.BASE64.decode("8pwwqGd9QvaB9idTkGvwqI8nEpu5ho/qHI/wRy5yUrM="))
    return Keys.hmacShaKeyFor(bytes);
  }

  private String generateSecretKey(){
    SecureRandom random = new SecureRandom();
    byte[] keyBytes = new byte[32];
    random.nextBytes(keyBytes);
    String secretKey = Encoders.BASE64.encode(keyBytes);
    return secretKey;
  }

  private Claims extractAllClaims(String token){
    return Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
    final Claims claims = this.extractAllClaims(token);
    return claimsTFunction.apply(claims);
  }

  // check expiration
  public boolean isTokenExpired(String token){
    Date expirationDate = this.extractClaim(token, Claims::getExpiration);
    return expirationDate.before(new Date());
  }

  public String extractPhoneNumber(String token){
    return extractClaim(token, Claims::getSubject);
  }

  public boolean validateToken(String token, User userDetails){
    String phoneNumber = extractPhoneNumber(token);
    Token existingToken = tokenRepository.findByToken(token);
    if(existingToken == null || existingToken.getIsRevoked() == true || !userDetails.isActive()) {
      return false;
    }
    return (phoneNumber.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }
}
