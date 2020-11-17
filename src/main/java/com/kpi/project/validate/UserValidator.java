package com.kpi.project.validate;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.model.enums.Role;
import com.kpi.project.model.exception.ValidatorException;
import com.kpi.project.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void userRolesUpdateValidator(Long userId, Set<String> roles) {
        for (String role : roles) {
            try {
                Role.valueOf(role);
            } catch (Exception e) {
                throw new ValidatorException(String.format("Not existing role: %s", role));
            }
        }
        if (Objects.isNull(userRepository.findByIdIdentifier(userId))) {
            throw new ValidatorException(String.format("User with id : %s, not exists", userId));
        }
    }

    public void validateUserHavePermission(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final Object principal = authentication.getPrincipal();
        final Long idFromToken = ((User) principal).getId();
        final Set<Role> roles = ((User) principal).getRoles();
        final boolean isAdmin = roles.stream().anyMatch(foo -> foo.toString().equals("ADMIN"));

        if (!isAdmin) {
            if (!id.equals(idFromToken)) {
                throw new ValidatorException("You do not have permission to change password");
            }
        }

    }

    public void validateUserPassword(UserDto userToValidate) {
        if (!Objects.equals(userToValidate.getPassword(), userToValidate.getMatchingPassword())) {
            throw new ValidatorException("Passwords does not match");
        }
        if (userToValidate.getPassword().length() < 4) {
            throw new ValidatorException("Password length must be minimum of 4 symbols");
        }
        if (Objects.isNull(userRepository.findByIdIdentifier(userToValidate.getId()))) {
            throw new ValidatorException(String.format("User with id : %s, not exists", userToValidate.getId()));
        }
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

        final User userByEmail = userRepository.loadUserByEmail(userToValidate.getEmail());
        final User userByUsername = userRepository.loadUserByUsername(userToValidate.getUsername());
        if (Objects.nonNull(userByEmail) && Objects.nonNull(userByUsername)) {
            if (Objects.equals(userByEmail.getEmail(), userToValidate.getEmail())) {
                throw new ValidatorException("Email already exists");
            }
            throw new ValidatorException("Username already exists");
        }
    }
}
