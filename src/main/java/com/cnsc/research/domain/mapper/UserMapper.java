package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.User;
import com.cnsc.research.domain.transaction.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends GeneralMapper<User, UserDto> {

    private final PasswordEncoder passwordEncoder;
    private boolean withPassword = false;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto toTransaction(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .role(user.getRole())
                .access(user.getAccess())
                .build();
    }

    public UserMapper withPassword() {
        this.withPassword = true;
        return this;
    }

    @Override
    public User toDomain(UserDto data) throws Exception {

        return User.builder()
                .id(data.getId())
                .username(data.getUsername())
                .name(data.getName())
                .role(data.getRole())
                .access(data.getAccess())
                .password(this.withPassword ? passwordEncoder.encode(data.getPassword()) : null)
                .build();
    }

}
