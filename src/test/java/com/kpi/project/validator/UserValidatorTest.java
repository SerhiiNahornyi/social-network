package com.kpi.project.validator;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.repository.UserRepository;
import com.kpi.project.validate.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator testingInstance;

    private UserDto user;

    @BeforeEach
    public void setUp() {
        user = new UserDto();
        user.setPassword("password");
        user.setMatchingPassword("password");
        user.setUsername("username");
        user.setEmail("email@mail.com");
    }


    @Test
    public void validateUserShouldThrowExceptionIfPasswordDoesNotMatch() {
        // given
        user.setPassword("");

        // expected
        assertThatIllegalArgumentException()
                .isThrownBy(() -> testingInstance.validateUser(user))
                .withMessage("Passwords does not match");
    }

    @Test
    public void validateUserShouldThrowExceptionIfPasswordHasIncorrectLength() {
        // given
        user.setPassword("pas");
        user.setMatchingPassword("pas");

        // expected
        assertThatIllegalArgumentException()
                .isThrownBy(() -> testingInstance.validateUser(user))
                .withMessage("Password length must be minimum of 4 symbols");
    }

    @Test
    public void validateUserShouldThrowExceptionIfEmailIsNotPresent() {
        // given
        user.setEmail("");

        // expected
        assertThatIllegalArgumentException()
                .isThrownBy(() -> testingInstance.validateUser(user))
                .withMessage("Email should be present");
    }

    @Test
    public void validateUserShouldThrowExceptionIfUserNameIsNotPresent() {
        // given
        user.setUsername("");

        // expected
        assertThatIllegalArgumentException()
                .isThrownBy(() -> testingInstance.validateUser(user))
                .withMessage("Username should be present");
    }

    @Test
    public void validateUserShouldThrowExceptionIfEmailAlreadyExists() {
        // given
        final User userModel = new User();
        userModel.setEmail("email@mail.com");
        given(userRepository.findByEmailOrUsername("email@mail.com", "username")).willReturn(userModel);

        // expected
        assertThatIllegalArgumentException()
                .isThrownBy(() -> testingInstance.validateUser(user))
                .withMessage("Email already exists");
    }

    @Test
    public void validateUserShouldThrowExceptionIfUserNameAlreadyExists() {
        // given
        final User userModel = new User();
        userModel.setUsername("username");
        given(userRepository.findByEmailOrUsername("email@mail.com", "username")).willReturn(userModel);

        // expected
        assertThatIllegalArgumentException()
                .isThrownBy(() -> testingInstance.validateUser(user))
                .withMessage("Username already exists");
    }
}
