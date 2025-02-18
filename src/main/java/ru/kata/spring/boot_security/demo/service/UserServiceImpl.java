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
    public void saveUser(User user, String[] roles) {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            Set<Role> userRoles = new HashSet<>();

            for (String role : roles) {
                Role existingRole = roleService.findByRoleName("ROLE_" + role)
                        .orElse(null);

                if (existingRole != null) {
                    userRoles.add(existingRole);
                } else {
                    Role newRole = new Role("ROLE_" + role);
                    userRoles.add(newRole);
                }
            }

            userRepository.save(new User(
                    user.getUsername(), passwordEncoder.encode(user.getPassword()),
                    user.getName(), user.getLastname(),
                    user.getCity(), 21, user.getEmail(),
                    userRoles
            ));
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(User user, String[] roles) {

        Set<Role> userRoles = new HashSet<>();

        for (String role : roles) {
            Role existingRole = roleService.findByRoleName("ROLE_" + role)
                    .orElse(null);

            if (existingRole != null) {
                userRoles.add(existingRole);
            } else {
                Role newRole = new Role("ROLE_" + role);
                userRoles.add(newRole);
            }
        }

        User userToSave = new User(
                user.getUsername(), passwordEncoder.encode(user.getPassword()),
                user.getName(), user.getLastname(),
                user.getCity(), user.getAge(), user.getEmail(),
                userRoles
        );

        userToSave.setId(user.getId());

        userRepository.save(userToSave);
    }
}
