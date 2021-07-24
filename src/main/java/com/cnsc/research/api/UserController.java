package com.cnsc.research.api;

import com.cnsc.research.domain.transaction.UserDto;
import com.cnsc.research.domain.exception.AccountNotFound;
import com.cnsc.research.domain.model.User;
import com.cnsc.research.domain.exception.AccountAlreadyExistException;
import com.cnsc.research.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/v1/account")
    public String addUser(@RequestBody User newUser) throws AccountAlreadyExistException {
        return userService.registerUser(newUser);
    }

    @GetMapping("/api/v1/account/{id}")
    public UserDto getUser(@PathVariable(name="id") int id) throws AccountNotFound {
        return userService.retrieveUserByid(id);
    }

    @GetMapping("/api/v1/account")
    public List<UserDto> getUsers()  {
        return userService.retrieveUsers();
    }
}
