package com.kpi.project.service;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.model.enums.Role;
import com.kpi.project.model.mapper.UserMapper;
import com.kpi.project.repository.UserRepository;
import com.kpi.project.util.JwtUtil;
import com.kpi.project.validate.UserValidator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserValidator userValidator,
                       UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto changeUserPassword(UserDto userDto) throws UsernameNotFoundException {
        userValidator.validateUserPassword(userDto);
        userValidator.validateUserHavePermission(userDto.getId());
        final User userWithNewPassword = userRepository.findByIdIdentifier(userDto.getId());
        final String newPassword = userDto.getMatchingPassword();
        userWithNewPassword.setPassword(passwordEncoder.encode(newPassword));

        return userMapper.userToDto(userRepository.save(userWithNewPassword));

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.userToDto(userRepository.save(user));
    }
}
