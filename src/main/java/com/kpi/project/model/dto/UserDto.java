package com.kpi.project.model.dto;

import com.kpi.project.model.User;
import com.kpi.project.model.post.Post;
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

    Set<User> friends;

    Set<Post> posts;

}
