package com.kpi.project.service;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.model.enums.Role;
import com.kpi.project.model.mapper.UserMapper;
import com.kpi.project.repository.UserRepository;
import com.kpi.project.validate.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserValidator userValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    private UserDto userDto;

    @InjectMocks
    private UserService testingInstance;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .email("mail@mail.com")
                .username("username")
                .password("password")
                .roles(Collections.singleton(Role.USER))
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .email("mail@mail.com")
                .username("username")
                .password("password")
                .matchingPassword("password")
                .build();
    }

    @Test
    public void loadUserByUsernameShouldReturnUserFoundByEmailOrUsername() {
        // given
        given(userRepository.loadByEmailOrUsername("login")).willReturn(user);

        // when
        final User actualUser = testingInstance.loadUserByUsername("login");

        // then
        verify(userRepository).loadByEmailOrUsername("login");
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    public void saveUserShouldReturnSavedUser() {
        // given
        final User newUser = user.toBuilder()
                .password("hashedPassword")
                .build();
        given(userMapper.dtoToUser(userDto)).willReturn(newUser);
        given(userMapper.userToDto(any(User.class))).willReturn(userDto);
        given(userRepository.save(any(User.class))).willReturn(newUser);
        given(passwordEncoder.encode("password")).willReturn("hashedPassword");

        // when
        final UserDto actualUser = testingInstance.saveUser(userDto);

        // then
        verify(userMapper).dtoToUser(userDto);
        verify(userMapper).userToDto(newUser);
        verify(userRepository).save(any(User.class));
        assertThat(actualUser).isEqualTo(userDto);
    }

    @Test
    public void updateUserRolesShouldReturnUpdatedUser() {
        // given
        final Set<String> updatedRoles = Stream.of("ADMIN", "USER")
                .collect(Collectors.toCollection(HashSet::new));
        final UserDto newUserDto = userDto.toBuilder()
                .roles(updatedRoles)
                .build();
        given(userRepository.save(any())).willReturn(user);
        given(userMapper.userToDto(user)).willReturn(newUserDto);
        given(userRepository.findByIdIdentifier(1L)).willReturn(user);

        // when
        final UserDto actualUser = testingInstance.updateUserRoles(newUserDto);

        // then
        assertThat(actualUser)
                .isNotNull()
                .extracting(UserDto::getRoles)
                .isEqualTo(updatedRoles);
    }

    @Test
    public void changeUserPasswordShouldUpdateUsersPassword() {
        // given
        final UserDto newUserDto = userDto.toBuilder()
                .password("passwordChange")
                .build();
        given(userRepository.save(any())).willReturn(user);
        given(userMapper.userToDto(user)).willReturn(newUserDto);
        given(userRepository.findByIdIdentifier(1L)).willReturn(user);

        // when
        final UserDto actualUser = testingInstance.changeUserPassword(newUserDto);

        // then
        assertThat(actualUser)
                .isNotNull()
                .extracting(UserDto::getPassword)
                .isEqualTo("passwordChange");
    }
}
