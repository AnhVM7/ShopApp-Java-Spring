package com.ptit.shopapp.controllers;

import com.ptit.shopapp.dtos.UserDTO;
import com.ptit.shopapp.dtos.UserLoginDTO;
import com.ptit.shopapp.models.User;
import com.ptit.shopapp.services.IUserService;
import com.ptit.shopapp.services.impl.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
  private final IUserService userService;
  @PostMapping("/register")
  public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO,
      BindingResult result){
    try {
      if(result.hasErrors()){
        List<String> errorMessages = result.getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();
        return ResponseEntity.badRequest().body(errorMessages);
      }
      if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
        return ResponseEntity.badRequest().body("password does not match");
      }
      User user = userService.createUser(userDTO);
      return ResponseEntity.ok(user);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
    try {
      // kiem tra thong tin dang nhap va sinh token
      String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
      // tra ve token trong response
      return ResponseEntity.ok(token);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
