package com.kpi.project.validate;

import com.kpi.project.model.User;
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

    public void validateUserPermissions(Long id) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final String userName = authentication != null ? authentication.getName() : null;
        final User userFromContext = userRepository.findByUsername(userName);
        final boolean isAdmin = userFromContext.getRoles().stream().anyMatch(role -> role == Role.ADMIN);

        if (!isAdmin && !id.equals(userFromContext.getId())) {
            throw new ValidatorException("You do not have permission to change password");
        }
    }

    public void validateUserExistence(Long id) {
        if (Objects.isNull(userRepository.findByIdIdentifier(id))) {
            throw new ValidatorException(String.format("User with id : %s, not exists", id));
        }
    }

    public void validateUserFriend(User friend, String friendName) {
        if (Objects.isNull(friend)) {
            throw new ValidatorException(String.format("User with username : %s, not exists", friendName));
        }
    }

    public void validatePassword(String password, String matchingPassword) {
        if (!Objects.equals(password, matchingPassword)) {
            throw new ValidatorException("Passwords does not match");
        }
        if (password.length() < 4) {
            throw new ValidatorException("Password length must be minimum of 4 symbols");
        }
    }

    public void validateUser(String userEmail, String userName) {
        if (StringUtils.isBlank(userEmail)) {
            throw new ValidatorException("Email should be present");
        }
        if (StringUtils.isBlank(userName)) {
            throw new ValidatorException("Username should be present");
        }

        final User userByEmail = userRepository.findByEmail(userEmail);
        final User userByUsername = userRepository.findByUsername(userName);
        if (Objects.nonNull(userByEmail)) {
            throw new ValidatorException("Email already exists");
        }
        if (Objects.nonNull(userByUsername)) {
            throw new ValidatorException("Username already exists");
        }
    }
}
