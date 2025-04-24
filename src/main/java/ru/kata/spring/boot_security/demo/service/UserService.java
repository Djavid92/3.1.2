package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto findByUsername(String username);

    Optional<UserDto> getUserById(Long id);

    void saveUser(User user);
    void saveUser(UserDto userDto, String[] roles);

    void deleteUser(Long id);

    void updateUser(UserDto userDto, String[] roles);
}
