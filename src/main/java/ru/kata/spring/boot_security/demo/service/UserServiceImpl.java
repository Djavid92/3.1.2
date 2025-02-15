package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserDao userRepository, PasswordEncoder passwordEncoder,
                           RoleService roleService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void saveUser(User user, String[] roles) {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            Set<Role> rolesToSet = new HashSet<>();

            for (String role : roles) {
                Role existingRole = roleService.findByRoleName("ROLE_" + role)
                        .orElse(null);

                if (existingRole != null) {
                    rolesToSet.add(existingRole);
                } else {
                    Role newRole = new Role("ROLE_" + role);
                    roleService.saveRole(newRole);
                    rolesToSet.add(newRole);
                }
            }

            user.setRoles(rolesToSet);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUser(User user, String[] roles) {
        Set<Role> rolesToSet = new HashSet<>();

        for (String role : roles) {
            Role existingRole = roleService.findByRoleName("ROLE_" + role)
                    .orElse(null);

            if (existingRole != null) {
                rolesToSet.add(existingRole);
            } else {
                Role newRole = new Role("ROLE_" + role);
                roleService.saveRole(newRole);
                rolesToSet.add(newRole);
            }
        }

        user.setRoles(rolesToSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }
}
