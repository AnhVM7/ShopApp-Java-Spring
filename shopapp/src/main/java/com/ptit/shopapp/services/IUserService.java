package com.ptit.shopapp.services;

import com.ptit.shopapp.dtos.UpdateUserDTO;
import com.ptit.shopapp.dtos.UserDTO;
import com.ptit.shopapp.exceptions.DataNotFoundException;
import com.ptit.shopapp.exceptions.InvalidPasswordException;
import com.ptit.shopapp.models.User;
import com.ptit.shopapp.responses.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
  User createUser(UserDTO userDTO) throws Exception;
  String login(String phoneNumber, String password) throws Exception;

  UserResponse getUserDetailsFromToken(String token) throws Exception;

  User updateUser(Long userId, UpdateUserDTO updatedUserDTO) throws Exception;

  Page<User> findAll(String keyword, Pageable pageable) throws Exception;

  void resetPassword(Long userId, String newPassword) throws DataNotFoundException, InvalidPasswordException;

  public void blockOrEnable(Long userId, Boolean active) throws DataNotFoundException;
}
