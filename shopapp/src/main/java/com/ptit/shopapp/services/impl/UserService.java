package com.ptit.shopapp.services.impl;

import com.ptit.shopapp.components.JwtTokenUtil;
import com.ptit.shopapp.dtos.UpdateUserDTO;
import com.ptit.shopapp.dtos.UserDTO;
import com.ptit.shopapp.exceptions.DataNotFoundException;
import com.ptit.shopapp.exceptions.PermissionDenyException;
import com.ptit.shopapp.models.Role;
import com.ptit.shopapp.models.User;
import com.ptit.shopapp.repositories.RoleRepository;
import com.ptit.shopapp.repositories.UserRepository;
import com.ptit.shopapp.responses.UserResponse;
import com.ptit.shopapp.services.IUserService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
  private final ModelMapper modelMapper;

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtil jwtTokenUtil;
  private final AuthenticationManager authenticationManager;
  @Override
  @Transactional
  public User createUser(UserDTO userDTO) throws Exception {
    // register user
    String phoneNumber = userDTO.getPhoneNumber();
    // kiem tra so dien thoai da ton tai chua
    if(userRepository.existsByPhoneNumber(phoneNumber)){
      throw new DataIntegrityViolationException("phone number already exists");
    }
    Role role = roleRepository.findById(userDTO.getRoleId())
        .orElseThrow(() -> new DataNotFoundException("Role not found"));
    if(role.getName().toUpperCase().equals("ADMIN")){
      throw new PermissionDenyException("you can't register an admin account");
    }
    // convert tu userDTO sang user
    User newUser = User
        .builder()
        .fullName(userDTO.getFullName())
        .phoneNumber(userDTO.getPhoneNumber())
        .password(userDTO.getPassword())
        .address(userDTO.getAddress())
        .dateOfBirth(userDTO.getDateOfBirth())
        .facebookAccountId(userDTO.getFacebookAccountId())
        .googleAccountId(userDTO.getGoogleAccountId())
        .build();

    newUser.setRole(role);
    //kiem tra neu co accountId thi ko yeu cau password
    if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
      String password = userDTO.getPassword();
      String encodedPassword = passwordEncoder.encode(password);
      newUser.setPassword(encodedPassword);
    }
    return userRepository.save(newUser);
  }

  @Override
  public String login(String phoneNumber, String password) throws Exception {
    Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
    if(userOptional.isEmpty()) {
      throw new DataNotFoundException("invalid phonenumber/ password");
    }
    User existingUser = userOptional.get();
    // check password
    if(existingUser.getFacebookAccountId() == 0
        && existingUser.getGoogleAccountId() == 0){
      if(!passwordEncoder.matches(password, existingUser.getPassword())){
        throw new BadCredentialsException("wrong phonenumber/ password");
      }
    }
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            phoneNumber, password, existingUser.getAuthorities()
        );

    // authenticate with java spring security
    authenticationManager.authenticate(authenticationToken);
    return jwtTokenUtil.generateToken(existingUser);
  }

  @Override
  public UserResponse getUserDetailsFromToken(String token) throws Exception {
    if(jwtTokenUtil.isTokenExpired(token)) {
      throw new Exception("Token is expired");
    }
    String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
    Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

    UserResponse userResponse = new UserResponse();
    if(user.isPresent()){
      modelMapper.map(user.get(), userResponse);
      return userResponse;
    } else {
      throw new Exception("User not found");
    }
  }

  @Transactional
  @Override
  public User updateUser(Long userId, UpdateUserDTO updatedUserDTO) throws Exception {
    // Find the existing user by userId
    User existingUser = userRepository.findById(userId)
        .orElseThrow(() -> new DataNotFoundException("User not found"));

    // Check if the phone number is being changed and if it already exists for another user
    String newPhoneNumber = updatedUserDTO.getPhoneNumber();
    if (!existingUser.getPhoneNumber().equals(newPhoneNumber) &&
        userRepository.existsByPhoneNumber(newPhoneNumber)) {
      throw new DataIntegrityViolationException("Phone number already exists");
    }

    // Update user information based on the DTO
    if (updatedUserDTO.getFullName() != null) {
      existingUser.setFullName(updatedUserDTO.getFullName());
    }
    if (newPhoneNumber != null) {
      existingUser.setPhoneNumber(newPhoneNumber);
    }
    if (updatedUserDTO.getAddress() != null) {
      existingUser.setAddress(updatedUserDTO.getAddress());
    }
    if (updatedUserDTO.getDateOfBirth() != null) {
      existingUser.setDateOfBirth(updatedUserDTO.getDateOfBirth());
    }
    if (updatedUserDTO.getFacebookAccountId() > 0) {
      existingUser.setFacebookAccountId(updatedUserDTO.getFacebookAccountId());
    }
    if (updatedUserDTO.getGoogleAccountId() > 0) {
      existingUser.setGoogleAccountId(updatedUserDTO.getGoogleAccountId());
    }

    // Update the password if it is provided in the DTO
    if (updatedUserDTO.getPassword() != null
        && !updatedUserDTO.getPassword().isEmpty()) {
      if(!updatedUserDTO.getPassword().equals(updatedUserDTO.getRetypePassword())) {
        throw new DataNotFoundException("Password and retype password not the same");
      }
      String newPassword = updatedUserDTO.getPassword();
      String encodedPassword = passwordEncoder.encode(newPassword);
      existingUser.setPassword(encodedPassword);
    }
    //existingUser.setRole(updatedRole);
    // Save the updated user
    return userRepository.save(existingUser);
  }

}
