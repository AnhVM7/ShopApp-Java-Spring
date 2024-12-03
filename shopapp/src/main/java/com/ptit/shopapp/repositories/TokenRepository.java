package com.ptit.shopapp.repositories;

import com.ptit.shopapp.models.Token;
import com.ptit.shopapp.models.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
  List<Token> findByUser(User user);
  Token findByToken(String token);
}
