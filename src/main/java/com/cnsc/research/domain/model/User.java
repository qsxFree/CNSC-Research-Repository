package com.cnsc.research.domain.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.cnsc.research.domain.model.Role.EDITOR;
import static com.cnsc.research.domain.model.Role.SUPER_ADMIN;

@Entity
@Table(name = "user")
@Data
public class User implements UserDetails {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;

    private String password;
    private String access;
    private String role;
    private boolean deleted;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> authorities = new HashSet<>();
        authorities.add(role.equals(SUPER_ADMIN.name()) ? SUPER_ADMIN : EDITOR);
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
