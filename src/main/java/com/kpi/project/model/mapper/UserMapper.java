package com.kpi.project.model.mapper;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User dtoToUser(UserDto userDto) {

        return User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .username(userDto.getUsername())
                .build();
    }

    public UserDto userToDto(User user) {
        final Set<String> newRoles = user.getRoles().stream()
                .map(Enum::toString)
                .collect(Collectors.toSet());

        return UserDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .roles(newRoles)
                .build();
    }
}
