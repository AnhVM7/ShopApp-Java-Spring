package com.ptit.shopapp.controllers;

import com.ptit.shopapp.dtos.UpdateUserDTO;
import com.ptit.shopapp.dtos.UserDTO;
import com.ptit.shopapp.dtos.UserLoginDTO;
import com.ptit.shopapp.exceptions.DataNotFoundException;
import com.ptit.shopapp.exceptions.InvalidPasswordException;
import com.ptit.shopapp.models.User;
import com.ptit.shopapp.responses.LoginResponse;
import com.ptit.shopapp.responses.RegisterResponse;
import com.ptit.shopapp.responses.UserListResponse;
import com.ptit.shopapp.responses.UserResponse;
import com.ptit.shopapp.services.ITokenService;
import com.ptit.shopapp.services.IUserService;
import com.ptit.shopapp.components.LocalizationUtil;
import com.ptit.shopapp.utils.MessageKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);
  private final IUserService userService;
  private final ITokenService tokenService;
  private final LocalizationUtil localizationUtil;
  private final ModelMapper modelMapper;

  @GetMapping("")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> getAllUser(
      @RequestParam(defaultValue = "", required = false) String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int limit
  ) {
    try {
      PageRequest pageRequest = PageRequest.of(page, limit,
          Sort.by("id").ascending());
      Page<User> users = userService.findAll(keyword, pageRequest);
      int totalPages = users.getTotalPages();
      List<UserResponse> userResponses = new ArrayList<>();
      // doi tu page sang list, sd ham getContent
      users.getContent().forEach(user -> {
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        userResponses.add(userResponse);
      });
      return ResponseEntity.ok(UserListResponse
          .builder()
          .userResponseList(userResponses)
          .totalPages(totalPages)
          .build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
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
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request){
    try {
      // kiem tra thong tin dang nhap va sinh token
      String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
      String userAgent = request.getHeader("User-Agent");
      UserResponse userResponse = userService.getUserDetailsFromToken(token);
      User user = new User();
      modelMapper.map(userResponse, user);
      tokenService.addToken(user, token, isMobileDevice(userAgent));

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

  private boolean isMobileDevice(String userAgent) {
    return userAgent.toLowerCase().contains("mobile");
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

  @PutMapping("/reset-password/{userId}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> resetPassword(@Valid @PathVariable Long userId){
    try {
      String newPassword = UUID.randomUUID().toString().substring(0, 6);
      userService.resetPassword(userId, newPassword);
      return ResponseEntity.ok(newPassword);
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body("user not found");
    } catch (InvalidPasswordException e) {
      return ResponseEntity.badRequest().body("invalid password");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/block/{userId}/{active}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> blockUser(@Valid @PathVariable Long userId,
      @Valid @PathVariable int active){
    try {
      userService.blockOrEnable(userId, active > 0);
      String message = active > 0 ? "enable sucessfully" : "block sucessfully";
      return ResponseEntity.ok().body(message);
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body("user not found");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
