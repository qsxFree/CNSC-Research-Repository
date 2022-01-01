package com.cnsc.research.domain.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.cnsc.research.domain.model.Role.EDITOR;
import static com.cnsc.research.domain.model.Role.SUPER_ADMIN;

@Entity
@Table(name = "user")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Column(name = "datetime_deleted")
    private LocalDateTime datetimeDeleted;

    @ManyToMany(mappedBy = "userList", fetch = FetchType.LAZY)
    private List<Notification> notificationList;

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
        return !deleted;
    }
}
