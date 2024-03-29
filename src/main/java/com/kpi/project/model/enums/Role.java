package com.kpi.project.model.enums;

import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("checkstyle:hideutilityclassconstructor")
public enum Role implements GrantedAuthority {

    ADMIN(Code.ADMIN),
    USER(Code.USER);

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public class Code {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_USER";
    }
}
