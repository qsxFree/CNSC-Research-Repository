package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.User;
import com.cnsc.research.domain.transaction.UserDto;
import org.springframework.stereotype.Component;


@Component
public class UserMapper extends GeneralMapper<User, UserDto> {

    public UserDto toTransaction(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getName());
    }

    @Override
    public User toDomain(UserDto transactionsData) throws Exception {
        return null;
    }

}
