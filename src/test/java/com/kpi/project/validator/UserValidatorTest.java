package com.kpi.project.validator;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.model.enums.Role;
import com.kpi.project.model.exception.ValidatorException;
import com.kpi.project.model.post.Post;
import com.kpi.project.repository.UserRepository;
import com.kpi.project.validate.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator testingInstance;

    @Test
    public void validatePostToAddShouldThrowExceptionIfAuthorIsNotPresent() {
        //given
        final Post givenPost = new Post("imageURL", "description", "comment", null);

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validatePostToAdd(givenPost))
                .withMessage("The post must include the author");
    }

    @Test
    public void validatePostToAddShouldThrowExceptionIfImageURLIsNotPresent() {
        //given
        final Post givenPost = new Post(null, "description", "comment", null);

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validatePostToAdd(givenPost))
                .withMessage("URL should be present");
    }

    @Test
    public void validatePostToAddShouldThrowExceptionIfDescriptionIsNotPresent() {
        //given
        final Post givenPost = new Post("imageURL", null, "comment", null);

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validatePostToAdd(givenPost))
                .withMessage("Description should be present");
    }

    @Test
    public void validatePasswordShouldThrowExceptionIfPasswordDoesNotMatch() {
        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validatePassword("password", "wrongPassword"))
                .withMessage("Passwords does not match");
    }

    @Test
    public void validatePasswordShouldThrowExceptionIfPasswordHasIncorrectLength() {
        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validatePassword("pa", "pa"))
                .withMessage("Password length must be minimum of 4 symbols");
    }

    @Test
    public void validateUserShouldThrowExceptionIfEmailIsNotPresent() {
        // given
        final UserDto givenUserDto = UserDto.builder().build();

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUser(givenUserDto))
                .withMessage("Email should be present");
    }

    @Test
    public void validateUserShouldThrowExceptionIfUserNameIsNotPresent() {
        // given
        final UserDto givenUserDto = UserDto.builder()
                .email("email@mail.com")
                .build();

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUser(givenUserDto))
                .withMessage("Username should be present");
    }

    @Test
    public void validateUserShouldThrowExceptionIfEmailAlreadyExists() {
        // given
        final User givenUser = givenUser(userBuilder -> userBuilder.email("email@mail.com"));
        final UserDto givenUserDto = UserDto.builder()
                .username("username")
                .email("email@mail.com")
                .build();

        given(userRepository.findByEmail("email@mail.com")).willReturn(givenUser);

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUser(givenUserDto))
                .withMessage("Email already exists");
    }

    @Test
    public void validateUserShouldThrowExceptionIfEmailNotValid() {
        // given
        final User givenUser = givenUser(userBuilder -> userBuilder.email("email@mail.com"));
        final UserDto givenUserDto = UserDto.builder()
                .username("username")
                .email("notValidEmail@111.fgh")
                .build();

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUser(givenUserDto))
                .withMessage("Not valid email");
    }

    @Test
    public void validateUserShouldThrowExceptionIfUserNameAlreadyExists() {
        // given
        final User givenUser = givenUser(userBuilder -> userBuilder.username("existingUserName"));
        final UserDto givenUserDto = UserDto.builder()
                .username("existingUserName")
                .email("email@mail.com")
                .build();

        given(userRepository.findByUsername("existingUserName")).willReturn(givenUser);

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUser(givenUserDto))
                .withMessage("Username already exists");
    }

    @Test
    public void validateUserShouldThrowExceptionIfUserIsNotExist() {
        // given
        final Set<String> roles = new HashSet<>(Arrays.asList("ADMIN", "USER"));

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.userRolesUpdateValidator(1L, roles))
                .withMessage("User with id : 1, not exists");
    }

    @Test
    public void validateUserShouldThrowExceptionIfRolesIsNotExist() {
        // given
        final Set<String> roles = new HashSet<>(Collections.singletonList("NOT_EXISTED_ROLE"));

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.userRolesUpdateValidator(1L, roles))
                .withMessage("Not existing role: NOT_EXISTED_ROLE");
    }

    @Test
    public void validateUserPermissionShouldThrowExceptionWhenUserIsNotAdmin() {
        // given
        final User givenUser = givenUser(userBuilder -> userBuilder.id(2L).roles(Collections.emptySet()));

        given(userRepository.findByUsername(any())).willReturn(givenUser);

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUserPermissions(1L))
                .withMessage("You do not have permission to change password");
    }

    @Test
    public void validateUserPermissionShouldNotThrowException() {
        // given
        final User givenUser = givenUser(userBuilder -> userBuilder.roles(Collections.emptySet()));

        given(userRepository.findByUsername(any())).willReturn(givenUser);

        // expected
        assertDoesNotThrow(() -> testingInstance.validateUserPermissions(1L));
    }

    @Test
    public void validateUserExistenceShouldThrowExceptionUserIsNotExist() {
        //given
        given(userRepository.findByIdIdentifier(any())).willReturn(null);

        //expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUserExistence(25L))
                .withMessage("User with id: 25, not exists");
    }

    @Test
    public void validateFriendToAddShouldThrowExceptionUserIsNotExist() {
        //expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateFriendToAdd(null, "username"))
                .withMessage("User with username: username, not exists");
    }

    @Test
    public void validateUserShouldThrowExceptionIfUserAgeIsUnderSixteen() {
        //given
        final UserDto givenUserDto = UserDto.builder()
                .username("existingUserName")
                .email("email@mail.com")
                .dateOfBirth(LocalDate.now().minusYears(16))
                .build();

        //expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUser(givenUserDto))
                .withMessage("Age restriction of sixteen years");
    }

    @Test
    public void validateUserShouldNotThrowExceptionIfUserIsValid() {
        //given
        final UserDto givenUserDto = UserDto.builder()
                .username("username")
                .email("email@mail.com")
                .dateOfBirth(LocalDate.now().minusYears(16).minusDays(1))
                .build();

        //expected
        assertDoesNotThrow(() -> testingInstance.validateUser(givenUserDto));
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
}
