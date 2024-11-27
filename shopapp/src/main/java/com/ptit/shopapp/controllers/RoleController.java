package com.ptit.shopapp.controllers;

import com.ptit.shopapp.models.Role;
import com.ptit.shopapp.services.IRoleService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
  private final IRoleService roleService;

  @GetMapping
  public ResponseEntity<?> getAllRoles(){
    List<Role> roleList = roleService.getAllRoles();
    return ResponseEntity.ok(roleList);
  }
}
