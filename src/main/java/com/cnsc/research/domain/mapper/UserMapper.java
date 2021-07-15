package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.transaction.UserDto;
import com.cnsc.research.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class UserMapper {

    public UserDto toUserDto(User user){
        return new UserDto(user.getId(), user.getUsername(), user.getName());
    }

    public List<UserDto> toUserDto(List<User> users){
        ArrayList<UserDto> userDtoLists = new ArrayList<>();
        users.forEach((item)-> userDtoLists.add(new UserDto(item.getId(),item.getUsername(),item.getName())));
        return userDtoLists;
    }
}
