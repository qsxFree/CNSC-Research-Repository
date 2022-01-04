package com.cnsc.research.api;

import com.cnsc.research.domain.exception.AccountNotFound;
import com.cnsc.research.domain.transaction.ChangePasswordDto;
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
    public ResponseEntity addUser(@RequestBody UserDto newUser) {
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

    @GetMapping("/username/{username}")
    public ResponseEntity getUserByUsername(@PathVariable(name = "username") String username) {
        return userService.retrieveUserByUsername(username);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable(name = "id") Integer id) {
        return userService.deleteUser(id);
    }

    @DeleteMapping("/list")
    public ResponseEntity deleteUsers(@RequestBody List<Long> idList) {
        return userService.deleteUsers(idList);
    }

    @GetMapping("/list")
    public List<UserDto> getUsers() {
        return userService.retrieveUsers();
    }

    @GetMapping("/validate/{username}")
    public ResponseEntity<String> validateUsername(@PathVariable String username) {
        return userService.checkUsername(username);
    }

    @PutMapping("/change-password")
    public ResponseEntity changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePassword(changePasswordDto);
    }
}
