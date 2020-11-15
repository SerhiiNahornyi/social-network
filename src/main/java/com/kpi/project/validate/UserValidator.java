package com.kpi.project.validate;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.model.enums.Role;
import com.kpi.project.model.exception.ValidatorException;
import com.kpi.project.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User userUpdateValidator(User user, Set<String> roles) {
        final User userWithNewRoles;

        if (Objects.isNull(user)) {
            throw new ValidatorException("User is not exist");
        } else {
            userWithNewRoles = user;

            final Set<Role> newRoles = roles.stream().map(Role::valueOf).collect(Collectors.toSet());

            userWithNewRoles.setRoles(newRoles);
        }

        return userWithNewRoles;
    }

    public void validateUser(UserDto userToValidate) {
        if (!Objects.equals(userToValidate.getPassword(), userToValidate.getMatchingPassword())) {
            throw new ValidatorException("Passwords does not match");
        }
        if (userToValidate.getPassword().length() < 4) {
            throw new ValidatorException("Password length must be minimum of 4 symbols");
        }
        if (StringUtils.isBlank(userToValidate.getEmail())) {
            throw new ValidatorException("Email should be present");
        }
        if (StringUtils.isBlank(userToValidate.getUsername())) {
            throw new ValidatorException("Username should be present");
        }
        final User user = userRepository.findByEmailOrUsername(userToValidate.getEmail(), userToValidate.getUsername());
        if (Objects.nonNull(user)) {
            if (Objects.equals(user.getEmail(), userToValidate.getEmail())) {
                throw new ValidatorException("Email already exists");
            }
            throw new ValidatorException("Username already exists");
        }
    }
}
