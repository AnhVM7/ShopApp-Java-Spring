package com.ptit.shopapp.repositories;

import com.ptit.shopapp.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByPhoneNumber(String phoneNumber);
  Optional<User> findByPhoneNumber(String phoneNumber);
}
