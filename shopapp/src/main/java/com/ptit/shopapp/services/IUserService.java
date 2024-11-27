package com.ptit.shopapp.services;

import com.ptit.shopapp.dtos.UpdateUserDTO;
import com.ptit.shopapp.dtos.UserDTO;
import com.ptit.shopapp.exceptions.DataNotFoundException;
import com.ptit.shopapp.models.User;
import com.ptit.shopapp.responses.UserResponse;
import jakarta.transaction.Transactional;

public interface IUserService {
  User createUser(UserDTO userDTO) throws Exception;
  String login(String phoneNumber, String password) throws Exception;

  UserResponse getUserDetailsFromToken(String token) throws Exception;

  @Transactional
  User updateUser(Long userId, UpdateUserDTO updatedUserDTO) throws Exception;
}
