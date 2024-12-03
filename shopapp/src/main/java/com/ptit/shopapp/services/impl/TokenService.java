package com.ptit.shopapp.services.impl;

import com.ptit.shopapp.models.Token;
import com.ptit.shopapp.models.User;
import com.ptit.shopapp.repositories.TokenRepository;
import com.ptit.shopapp.services.ITokenService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
  private static final int MAX_TOKENS = 3;
  @Value("2592000")
  private int expiration;
  private final TokenRepository tokenRepository;

  @Transactional
  @Override
  public void addToken(User user, String token, boolean isMobileDevice){
    List<Token> userTokens = tokenRepository.findByUser(user);
    int tokenNum = userTokens.size();
    if (tokenNum >= MAX_TOKENS) {
      // kiem tra xem trong danh sach userTokens co ton tai it nhat
      // mot token khong phai la thiet bi di dong
      boolean hasNonMobileToken = !userTokens.stream().allMatch(Token::isMobile);
      Token tokenToDelete;
      if (hasNonMobileToken) {
        tokenToDelete = userTokens.stream().filter(userToken -> !userToken.isMobile())
            .findFirst().orElse(userTokens.get(0));
      } else {
        tokenToDelete = userTokens.get(0);
      }
      tokenRepository.delete(tokenToDelete);
    }
    Long expirationInSeconds = expiration * 1000L;
    LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);
    // tao moi 1 token cho nguoi dung
    Token newToken = Token.builder()
        .user(user)
        .token(token)
        .isRevoked(false)
        .isExpired(false)
        .tokenType("Bearer")
        .expirationDate(expirationDateTime)
        .isMobile(isMobileDevice)
        .build();
    tokenRepository.save(newToken);
  }

}
