package ru.kata.spring.boot_security.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.dto.UserMapping;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Convertor convertor;
    private final UserMapping userMapping;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(userMapping::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        return userMapping.toDto(userRepository.findByUsername(username));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).stream().map(userMapping::toDto).findFirst();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void saveUser(UserDto userDto, String[] roles) {
        if (userRepository.findByEmail(userDto.getEmail()) == null) {
            userRepository.save(new User(
                    userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()),
                    userDto.getName(), userDto.getLastname(),
                    userDto.getCity(), userDto.getAge(), userDto.getEmail(),
                    convertor.stringToSet(roles)
            ));
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUser(UserDto userDto, String[] roles) {
        User userToSave = new User(
                userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()),
                userDto.getName(), userDto.getLastname(),
                userDto.getCity(), userDto.getAge(), userDto.getEmail(),
                convertor.stringToSet(roles)
        );

        userToSave.setId(userDto.getId());

        userRepository.save(userToSave);
    }
}