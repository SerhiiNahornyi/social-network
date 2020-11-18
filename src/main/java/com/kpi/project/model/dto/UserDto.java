package com.kpi.project.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder(toBuilder = true)
@Value
public class UserDto {

    Long id;

    String username;

    String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    String matchingPassword;

    Set<String> roles;
}
