package ru.kata.spring.boot_security.demo.dto;

import lombok.Data;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Set;
@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String lastname;
    private String city;
    private int age;
    private String email;
    private Set<Role> roles;
}
