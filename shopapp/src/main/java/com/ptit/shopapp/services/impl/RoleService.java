package com.ptit.shopapp.services.impl;

import com.ptit.shopapp.models.Role;
import com.ptit.shopapp.repositories.RoleRepository;
import com.ptit.shopapp.services.IRoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
  private final RoleRepository roleRepository;

  @Override
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }
}
