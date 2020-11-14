package com.kpi.project.validate;

import com.kpi.project.model.User;
import com.kpi.project.model.dto.UserDto;
import com.kpi.project.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserValidator {

    @Autowired
    private UserRepository userRepository;

    public void validateUser(UserDto userToValidate) {
        if (!Objects.equals(userToValidate.getPassword(), userToValidate.getMatchingPassword())) {
            throw new IllegalArgumentException("Passwords does not match");
        }
        if (userToValidate.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password length must be minimum of 4 symbols");
        }
        if (StringUtils.isBlank(userToValidate.getEmail())) {
            throw new IllegalArgumentException("Email should be present");
        }
        if (StringUtils.isBlank(userToValidate.getUsername())) {
            throw new IllegalArgumentException("Username should be present");
        }
        final User user = userRepository.findByEmailOrUsername(userToValidate.getEmail(), userToValidate.getUsername());
        if (Objects.nonNull(user)) {
            if (Objects.equals(user.getEmail(), userToValidate.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            throw new IllegalArgumentException("Username already exists");
        }
    }
}
