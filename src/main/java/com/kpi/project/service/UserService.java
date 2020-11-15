package com.kpi.project.service;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.model.enums.Role;
import com.kpi.project.model.mapper.UserMapper;
import com.kpi.project.repository.UserRepository;
import com.kpi.project.validate.UserValidator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserValidator userValidator, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
    }

    public UserDto updateUserRoles(UserDto userDto) throws UsernameNotFoundException {
        userValidator.userRolesUpdateValidator(userDto.getId(), userDto.getRoles());

        final User userWithNewRoles = userRepository.findByIdIdentifier(userDto.getId());
        final Set<Role> newRoles = userDto.getRoles().stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
        userWithNewRoles.setRoles(newRoles);

        return userMapper.userToDto(userRepository.save(userWithNewRoles));
    }

    @Override
    public User loadUserByUsername(String login) throws UsernameNotFoundException {

        return userRepository.loadByEmailOrUsername(login);
    }

    public UserDto saveUser(UserDto userDto) {
        userValidator.validateUser(userDto);
        final User user = userMapper.dtoToUser(userDto);
        user.setRoles(Collections.singleton(Role.USER));

        return userMapper.userToDto(userRepository.save(user));
    }
}
