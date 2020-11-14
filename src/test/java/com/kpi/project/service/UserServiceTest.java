package com.kpi.project.service;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.model.mapper.UserMapper;
import com.kpi.project.model.userRole.Role;
import com.kpi.project.repository.UserRepository;
import com.kpi.project.validate.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
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

    private User user;

    @InjectMocks
    private UserService testingInstance;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "mail@mail.com", "username",
                "password", Collections.singleton(Role.ADMIN));
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
    public void loadUserByUsernameShouldReturnSavedUser() {
        // given
        final UserDto userDto = new UserDto();
        userDto.setPassword("password");
        userDto.setMatchingPassword("password");
        userDto.setUsername("username");
        userDto.setEmail("mail@mail.com");

        given(userMapper.dtoToUser(userDto)).willReturn(user);
        given(userMapper.userToDto(user)).willReturn(userDto);
        given(userRepository.save(user)).willReturn(user);

        // when
        final UserDto actualUser = testingInstance.saveUser(userDto);

        // then
        verify(userMapper).dtoToUser(userDto);
        verify(userMapper).userToDto(user);
        verify(userRepository).save(user);
        assertThat(actualUser).isEqualTo(userDto);
    }
}
