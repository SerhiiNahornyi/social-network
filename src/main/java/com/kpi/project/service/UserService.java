package com.kpi.project.service;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.model.mapper.UserMapper;
import com.kpi.project.model.enums.Role;
import com.kpi.project.repository.UserRepository;
import com.kpi.project.validate.UserValidator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserValidator userValidator,
                       UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User loadUserByUsername(String login) throws UsernameNotFoundException {

        return userRepository.loadByEmailOrUsername(login);
    }

    public UserDto saveUser(UserDto userDto) {
        userValidator.validateUser(userDto);
        final User user = userMapper.dtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(Role.USER));

        return userMapper.userToDto(userRepository.save(user));
    }
}
