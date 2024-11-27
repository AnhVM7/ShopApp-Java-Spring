package com.ptit.shopapp.controllers;

import com.ptit.shopapp.dtos.UpdateUserDTO;
import com.ptit.shopapp.dtos.UserDTO;
import com.ptit.shopapp.dtos.UserLoginDTO;
import com.ptit.shopapp.models.User;
import com.ptit.shopapp.responses.LoginResponse;
import com.ptit.shopapp.responses.RegisterResponse;
import com.ptit.shopapp.responses.UserResponse;
import com.ptit.shopapp.services.IUserService;
import com.ptit.shopapp.components.LocalizationUtil;
import com.ptit.shopapp.utils.MessageKey;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);
  private final IUserService userService;
  private final LocalizationUtil localizationUtil;
  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserDTO userDTO,
      BindingResult result){
    try {
      if(result.hasErrors()){
        List<String> errorMessages = result.getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();
        return ResponseEntity.badRequest().body(
            RegisterResponse
                .builder()
                .message(localizationUtil.getLocalizedMessage(MessageKey.REGISTER_FAILED, errorMessages))
                .build()
        );
      }
      if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
        return ResponseEntity.badRequest().body(RegisterResponse
            .builder()
            .message(localizationUtil.getLocalizedMessage(MessageKey.PASSWORD_NOT_MATCH))
            .build());
      }
      User user = userService.createUser(userDTO);
      return ResponseEntity.ok(RegisterResponse
          .builder()
          .message(localizationUtil.getLocalizedMessage(MessageKey.REGISTER_SUCCESSFULLY))
          .user(user)
          .build());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegisterResponse
          .builder()
          .message(localizationUtil.getLocalizedMessage(MessageKey.REGISTER_FAILED, e.getMessage()))
          .build());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
    try {
      // kiem tra thong tin dang nhap va sinh token
      String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
      logger.info(token);
      // tra ve token trong response
      return ResponseEntity.ok(LoginResponse
          .builder()
          .message(localizationUtil.getLocalizedMessage(MessageKey.LOGIN_SUCCESSFULLY))
          .token(token)
          .build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(LoginResponse
          .builder()
          .message(localizationUtil.getLocalizedMessage(MessageKey.LOGIN_FAILED, e.getMessage()))
          .build());
    }
  }

  @PostMapping("/details")
  public ResponseEntity<UserResponse> getUserDetails(@RequestHeader("Authorization") String authorizationHeader) {
    try {
      String extractedToken = authorizationHeader.substring(7); // loai bo Bearer tu chuoi token
      UserResponse userResponse = userService.getUserDetailsFromToken(extractedToken);
      return ResponseEntity.ok(userResponse);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/details/{userId}")
  public ResponseEntity<UserResponse> updateUserDetails(
      @PathVariable Long userId,
      @RequestHeader("Authorization") String authorizationHeader,
      @RequestBody UpdateUserDTO updateUserDTO
  ) {
    try {
      String extractedToken = authorizationHeader.substring(7);
      UserResponse userResponse = userService.getUserDetailsFromToken(extractedToken);
      if (userResponse.getId() != userId) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
      User updatedUser = userService.updateUser(userId, updateUserDTO);
      return ResponseEntity.ok(UserResponse
          .builder()
          .id(updatedUser.getId())
          .fullName(updatedUser.getFullName())
          .phoneNumber(updatedUser.getPhoneNumber())
          .address(updatedUser.getAddress())
          .active(updatedUser.isActive())
          .dateOfBirth(updatedUser.getDateOfBirth())
          .facebookAccountId(updatedUser.getFacebookAccountId())
          .googleAccountId(updatedUser.getGoogleAccountId())
          .role(updatedUser.getRole())
          .build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

}
