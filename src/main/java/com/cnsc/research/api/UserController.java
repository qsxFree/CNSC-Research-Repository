package com.cnsc.research.api;

import com.cnsc.research.domain.exception.AccountAlreadyExistException;
import com.cnsc.research.domain.exception.AccountNotFound;
import com.cnsc.research.domain.model.User;
import com.cnsc.research.domain.transaction.UserDto;
import com.cnsc.research.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String addUser(@RequestBody User newUser) throws AccountAlreadyExistException {
        return userService.registerUser(newUser);
    }

    @PutMapping
    public ResponseEntity updateUser(@RequestBody UserDto user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable(name = "id") Integer id) throws AccountNotFound {
        return userService.retrieveUserByid(id);
    }

    @GetMapping("/list")
    public List<UserDto> getUsers() {
        return userService.retrieveUsers();
    }
}
