package com.kpi.project.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kpi.project.model.User;
import com.kpi.project.model.post.Post;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class UserDto {

    Long id;

    String username;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate dateOfBirth;

    String email;

    String password;

    String matchingPassword;

    Set<String> roles;

    Set<User> friends;

    Set<Post> posts;

}
