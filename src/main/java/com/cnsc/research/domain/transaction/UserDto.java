package com.cnsc.research.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private long id;
    private String username;
    private String name;
    private String role;
    private String access;
    private String password;
}
