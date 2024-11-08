package com.ptit.shopapp.services;

import com.ptit.shopapp.dtos.UserDTO;
import com.ptit.shopapp.exceptions.DataNotFoundException;
import com.ptit.shopapp.models.User;

public interface IUserService {
  User createUser(UserDTO userDTO) throws Exception;
  String login(String phoneNumber, String password) throws Exception;
}
