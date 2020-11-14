package com.kpi.project.service;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.model.mapper.UserMapper;
import com.kpi.project.model.userRole.Role;
import com.kpi.project.repository.UserRepository;
import com.kpi.project.validate.UserValidator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

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

    public User updateUserRoles(UserDto userDto) throws UsernameNotFoundException {
        final Optional<User> user = userRepository.findById(userDto.getId());

        return userRepository.save(userValidator.optionlValidator(user, userDto.getRoles()));
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
