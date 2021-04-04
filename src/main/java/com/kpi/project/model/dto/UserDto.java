package com.kpi.project.model.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder(toBuilder = true)
public class UserDto {

    Long id;

    String username;

    String email;

    String password;

    String matchingPassword;

    Set<String> roles;

    String friend;

}
