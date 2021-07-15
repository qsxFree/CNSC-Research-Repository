package com.cnsc.research.domain.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    SUPER_ADMIN,
    EDITOR;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
