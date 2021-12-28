package com.cnsc.research.service;

import com.cnsc.research.domain.exception.AccountAlreadyExistException;
import com.cnsc.research.domain.exception.AccountNotFound;
import com.cnsc.research.domain.mapper.UserMapper;
import com.cnsc.research.domain.model.User;
import com.cnsc.research.domain.repository.UserRepository;
import com.cnsc.research.domain.transaction.UserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final Logger logger;

    public String registerUser(User newUser) throws AccountAlreadyExistException {
        if (accountExist(newUser.getUsername())) {
            throw new AccountAlreadyExistException(newUser.getUsername());
        } else {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            userRepository.save(newUser);
            return "Account added";
        }
    }

    private boolean accountExist(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        return user.isPresent();
    }

    public UserDto retrieveUserByid(int id) throws AccountNotFound {
        Optional<User> user = userRepository.findById((long) id);
        if (user.isEmpty()) throw new AccountNotFound(id);
        return mapper.toTransaction(user.get());
    }

    public List<UserDto> retrieveUsers() {
        return mapper.toTransaction(userRepository.findAll());
    }

    public ResponseEntity updateUser(UserDto userDto) {
        try {
            User user = mapper.toDomain(userDto);
            userRepository.save(user);
            return new ResponseEntity("User has been updated", OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity("Error on updating user", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity deleteUser(Integer id) {
        try {
            Optional<User> optionalUser = userRepository.findById(Long.valueOf(id));
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setDeleted(true);
                userRepository.save(user);
                return new ResponseEntity("User has been deleted", OK);
            } else {
                return new ResponseEntity("Cannot delete user", BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity("Error on deleting user", INTERNAL_SERVER_ERROR);
        }
    }
}
