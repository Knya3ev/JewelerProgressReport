package com.example.JewelerProgressReport.users.enums.roles;


import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Stream;

public enum Role implements GrantedAuthority {
    ROLE_SUPER_USER("SUR"),
    ROLE_GUEST("GST"),
    ROLE_ADMIN("ADM"),
    ROLE_JEWELER("JWR"),
    ROLE_SHOP_ASSISTANT("SAT"),
    ROLE_DIRECTOR("DCR");

    private String code;

    Role(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Role fromCode(String code) {
        return Stream.of(Role.values())
                .filter(s -> s.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
