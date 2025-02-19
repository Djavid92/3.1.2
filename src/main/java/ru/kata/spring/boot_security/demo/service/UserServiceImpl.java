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
    private final Convertor convertor;

    @Autowired
    public UserServiceImpl(UserDao userRepository, PasswordEncoder passwordEncoder,
                           RoleService roleService, Convertor convertor)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.convertor = convertor;
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

    /**
     * Если говорить честно, то я вообще не понимаю как мне не назначать роли тут,
     * ручками, поэтому сделал конвертор. Я понимаю, что вы хотите, чтобы я
     * сразуполучал полного пользователя, но как???
    */

    @Override
    public void saveUser(User user, String[] roles) {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            userRepository.save(new User(
                    user.getUsername(), passwordEncoder.encode(user.getPassword()),
                    user.getName(), user.getLastname(),
                    user.getCity(), user.getAge(), user.getEmail(),
                    convertor.stringToSet(roles)
            ));
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(User user, String[] roles) {
        User userToSave = new User(
                user.getUsername(), passwordEncoder.encode(user.getPassword()),
                user.getName(), user.getLastname(),
                user.getCity(), user.getAge(), user.getEmail(),
                convertor.stringToSet(roles)
        );

        userToSave.setId(user.getId());

        userRepository.save(userToSave);
    }
}