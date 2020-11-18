package com.kpi.project.model.authentication;

import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class AuthenticationRequest {

    String username;
    String password;
}
