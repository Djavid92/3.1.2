package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.dto.UserMapping;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserMapping userMapping;
    private UserService userService;
    private RoleService roleService;
    private UserDto userDto;

    @Autowired
    public DataInitializer(UserService userService, RoleService roleService, UserMapping userMapping) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapping = userMapping;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        Role userRole = roleService.findByRoleName("ROLE_USER").orElseGet(() -> {
            Role newUserRole = new Role("ROLE_USER");
            roleService.saveRole(newUserRole);
            return newUserRole;
        });

        Role adminRole = roleService.findByRoleName("ROLE_ADMIN").orElseGet(() -> {
            Role newAdminRole = new Role("ROLE_ADMIN");
            roleService.saveRole(newAdminRole);
            return newAdminRole;
        });

        if (userService.findByUsername("user") == null) {
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            User user = new User("user", "user",
                    "Иван", "Иванович", "Москва",
                    21, "ivan@mail.ru", userRoles);
            userService.saveUser(user);
        }

        if (userService.findByUsername("admin") == null) {
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminRoles.add(userRole);
            User admin = new User("admin", "admin",
                    "Андрей", "Андреевич", "Санкт-Петербург",
                    21, "andrey@mail.ru", adminRoles);
            userService.saveUser(admin);
        }
    }
}
