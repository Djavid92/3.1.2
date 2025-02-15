package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByRoleName(String roleName);
    void saveRole(Role role);
}
