package ru.kata.spring.boot_security.demo.dto;

import org.mapstruct.Mapper;
import ru.kata.spring.boot_security.demo.models.User;

@Mapper(componentModel = "spring")
public interface UserMapping {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
