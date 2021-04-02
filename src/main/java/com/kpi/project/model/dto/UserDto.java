package com.kpi.project.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String password;

    private String matchingPassword;

    private Set<String> roles;

}
