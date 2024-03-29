package com.kpi.project.model.dto;

import com.kpi.project.model.User;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class UserDto {

    Long id;

    String username;

    LocalDate dateOfBirth;

    String email;

    String password;

    String matchingPassword;

    Set<String> roles;

    Set<User> friends;
}
