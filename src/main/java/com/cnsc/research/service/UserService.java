package com.cnsc.research.service;

import com.cnsc.research.domain.exception.AccountAlreadyExistException;
import com.cnsc.research.domain.exception.AccountNotFound;
import com.cnsc.research.domain.mapper.UserMapper;
import com.cnsc.research.domain.model.User;
import com.cnsc.research.domain.repository.UserRepository;
import com.cnsc.research.domain.transaction.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;



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

    public List<UserDto> retrieveUsers(){
        return mapper.toTransaction(userRepository.findAll());
    }
}
