package com.ptit.shopapp.services;

import com.ptit.shopapp.models.User;
import jakarta.transaction.Transactional;

public interface ITokenService {

  @Transactional
  void addToken(User user, String token, boolean isMobileDevice);
}
