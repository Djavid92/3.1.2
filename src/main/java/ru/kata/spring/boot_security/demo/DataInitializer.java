package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
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
