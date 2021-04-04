package com.kpi.project.service;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.model.enums.Role;
import com.kpi.project.model.mapper.UserMapper;
import com.kpi.project.repository.UserRepository;
import com.kpi.project.validate.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
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

    @InjectMocks
    private UserService testingInstance;

    @Test
    public void loadUserByUsernameShouldReturnUserFoundByEmailOrUsername() {
        // given
        final User givenUser = givenUser(identity());

        given(userRepository.loadByEmailOrUsername("login")).willReturn(givenUser);

        // when
        final User actualUser = testingInstance.loadUserByUsername("login");

        // then
        verify(userRepository).loadByEmailOrUsername("login");
        assertThat(actualUser).isEqualTo(givenUser);
    }

    @Test
    public void saveUserShouldReturnSavedUser() {
        // given
        final User givenUser = givenUser(userBuilder -> userBuilder.password("hashedPassword"));
        final UserDto givenUserDto = givenUserDto(identity());

        given(userMapper.dtoToUser(givenUserDto)).willReturn(givenUser);
        given(userMapper.userToDto(any(User.class))).willReturn(givenUserDto);
        given(userRepository.save(any(User.class))).willReturn(givenUser);
        given(passwordEncoder.encode("password")).willReturn("hashedPassword");

        // when
        final UserDto actualUser = testingInstance.saveUser(givenUserDto);

        // then
        verify(userMapper).dtoToUser(givenUserDto);
        verify(userMapper).userToDto(givenUser);
        verify(userRepository).save(any(User.class));
        assertThat(actualUser).isEqualTo(givenUserDto);
    }

    @Test
    public void updateUserRolesShouldReturnUpdatedUser() {
        // given
        final Set<String> updatedRoles = Stream.of("ADMIN", "USER")
                .collect(Collectors.toCollection(HashSet::new));
        final UserDto givenUserDto = givenUserDto(userDtoBuilder -> userDtoBuilder.roles(updatedRoles));
        final User givenUser = givenUser(identity());

        given(userRepository.save(any())).willReturn(givenUser);
        given(userMapper.userToDto(givenUser)).willReturn(givenUserDto);
        given(userRepository.findByIdIdentifier(1L)).willReturn(givenUser);

        // when
        final UserDto actualUser = testingInstance.updateUserRoles(givenUserDto);

        // then
        assertThat(actualUser)
                .isNotNull()
                .extracting(UserDto::getRoles)
                .isEqualTo(updatedRoles);
    }

    @Test
    public void addUserFriendShouldUpdateUsersFriends() {
        // given
        final UserDto givenUserDto = givenUserDto(userDtoBuilder -> userDtoBuilder.friend("newFriend"));
        final User givenUser = givenUser(identity());

        given(userRepository.save(any())).willReturn(givenUser);
        given(userMapper.userToDto(givenUser)).willReturn(givenUserDto);
        given(userRepository.findByUsername(givenUserDto.getUsername())).willReturn(givenUser);

        // when
        final UserDto actualUser = testingInstance.addUserFriend(givenUserDto.getUsername(), "newFriend");

        // then
        assertThat(actualUser)
                .isNotNull()
                .extracting(UserDto::getFriend)
                .isEqualTo("newFriend");
    }

    @Test
    public void changeUserPasswordShouldUpdateUsersPassword() {
        // given
        final UserDto givenUserDto = givenUserDto(userDtoBuilder -> userDtoBuilder.password("passwordChange"));
        final User givenUser = givenUser(identity());

        given(userRepository.save(any())).willReturn(givenUser);
        given(userMapper.userToDto(givenUser)).willReturn(givenUserDto);
        given(userRepository.findByIdIdentifier(1L)).willReturn(givenUser);

        // when
        final UserDto actualUser = testingInstance.changeUserPassword(givenUserDto);

        // then
        assertThat(actualUser)
                .isNotNull()
                .extracting(UserDto::getPassword)
                .isEqualTo("passwordChange");
    }

    private static User givenUser(Function<User.UserBuilder, User.UserBuilder> userCustomizer) {
        return userCustomizer.apply(User.builder()
                .id(1L)
                .email("mail@mail.com")
                .username("username")
                .password("password")
                .roles(Collections.singleton(Role.USER)))
                .build();
    }

    private static UserDto givenUserDto(Function<UserDto.UserDtoBuilder, UserDto.UserDtoBuilder> userDtoCustomizer) {
        return userDtoCustomizer.apply(UserDto.builder()
                .id(1L)
                .email("mail@mail.com")
                .username("username")
                .password("password")
                .matchingPassword("password"))
                .build();
    }
}
