package com.kpi.project.service;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.model.enums.Role;
import com.kpi.project.model.mapper.UserMapper;
import com.kpi.project.repository.UserRepository;
import com.kpi.project.validate.UserValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    public UserDto changeUserPassword(UserDto userDto) throws UsernameNotFoundException {
        final String password = userDto.getPassword();
        userValidator.validatePassword(password, userDto.getMatchingPassword());
        final Long userId = userDto.getId();
        userValidator.validateUserExistence(userId);
        userValidator.validateUserPermissions(userId);
        final User updatedUser = userRepository.findByIdIdentifier(userId).toBuilder()
                .password(passwordEncoder.encode(password))
                .build();

        return userMapper.userToDto(userRepository.save(updatedUser));
    }

    public UserDto updateUserRoles(UserDto userDto) throws UsernameNotFoundException {
        userValidator.userRolesUpdateValidator(userDto.getId(), userDto.getRoles());
        final Set<Role> newRoles = userDto.getRoles().stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
        final User userWithNewRoles = userRepository.findByIdIdentifier(userDto.getId()).toBuilder()
                .roles(newRoles)
                .build();

        return userMapper.userToDto(userRepository.save(userWithNewRoles));
    }

    public UserDto addUserFriend(String friendName) throws UsernameNotFoundException {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        final String username = authentication != null ? authentication.getName() : null;
        final User userFromToken = userRepository.findByUsername(username);
        final User userFriend = userRepository.findByUsername(friendName);

        userValidator.validateFriendToAdd(userFriend, friendName);

        final Set<User> updatedFriends = userFromToken.getFriends() != null
                ? userFromToken.getFriends()
                : new HashSet<>();
        updatedFriends.add(userFriend);

        final User userWithNewFriend = userRepository.findByUsername(username).toBuilder()
                .friends(updatedFriends)
                .build();

        return userMapper.userToDto(userRepository.save(userWithNewFriend));
    }

    @Override
    public User loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.loadByEmailOrUsername(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDto saveUser(UserDto userDto) {
        final String userPassword = userDto.getPassword();
        userValidator.validatePassword(userPassword, userDto.getMatchingPassword());
        userValidator.validateUser(userDto);
        final User userToSave = userMapper.dtoToUser(userDto).toBuilder()
                .password(passwordEncoder.encode(userPassword))
                .roles(Collections.singleton(Role.USER))
                .build();

        return userMapper.userToDto(userRepository.save(userToSave));
    }
}
