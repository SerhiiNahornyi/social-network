package com.kpi.project.validator;

import com.kpi.project.model.User;
import com.kpi.project.model.enums.Role;
import com.kpi.project.model.exception.ValidatorException;
import com.kpi.project.repository.UserRepository;
import com.kpi.project.validate.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator testingInstance;

    @Test
    public void validateUserShouldThrowExceptionIfPasswordDoesNotMatch() {
        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validatePassword("password", "wrongPassword"))
                .withMessage("Passwords does not match");
    }

    @Test
    public void validateUserShouldThrowExceptionIfPasswordHasIncorrectLength() {
        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validatePassword("pa", "pa"))
                .withMessage("Password length must be minimum of 4 symbols");
    }

    @Test
    public void validateUserShouldThrowExceptionIfEmailIsNotPresent() {
        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUser("", "username"))
                .withMessage("Email should be present");
    }

    @Test
    public void validateUserShouldThrowExceptionIfUserNameIsNotPresent() {
        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUser("email@mail.com", ""))
                .withMessage("Username should be present");
    }

    @Test
    public void validateUserShouldThrowExceptionIfEmailAlreadyExists() {
        // given
        final User givenUser = givenUser(userBuilder -> userBuilder.email("email@mail.com"));

        given(userRepository.findByEmail("email@mail.com")).willReturn(givenUser);

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUser("email@mail.com", "username"))
                .withMessage("Email already exists");
    }

    @Test
    public void validateUserShouldThrowExceptionIfUserNameAlreadyExists() {
        // given
        final User givenUser = givenUser(userBuilder -> userBuilder.username("existingUserName"));

        given(userRepository.findByUsername("existingUserName")).willReturn(givenUser);

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUser("email@mail.com", "existingUserName"))
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
    public void validateUserHavePermissionShouldThrowExceptionWhenUserIsNotAdmin() {
        // given
        final User givenUser = givenUser(userBuilder -> userBuilder.id(2L).roles(Collections.emptySet()));

        given(userRepository.findByUsername(any())).willReturn(givenUser);

        // expected
        assertThatExceptionOfType(ValidatorException.class)
                .isThrownBy(() -> testingInstance.validateUserPermissions(1L))
                .withMessage("You do not have permission to change password");
    }

    @Test
    public void validateUserHavePermissionShouldNotThrowException() {
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
                .withMessage("User with id : 25, not exists");
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
