package com.kpi.project.util.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class JwtProperties {

    private String signingKey;
    private int tokenExpirationTime;
}
