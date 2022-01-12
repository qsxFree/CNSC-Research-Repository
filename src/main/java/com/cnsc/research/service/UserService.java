package com.cnsc.research.service;

import com.cnsc.research.configuration.security.JwtTokenUtil;
import com.cnsc.research.domain.exception.AccountNotFound;
import com.cnsc.research.domain.mapper.UserMapper;
import com.cnsc.research.domain.model.User;
import com.cnsc.research.domain.repository.UserRepository;
import com.cnsc.research.domain.transaction.AuthenticationResponse;
import com.cnsc.research.domain.transaction.ChangePasswordDto;
import com.cnsc.research.domain.transaction.UserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final Logger logger;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public ResponseEntity registerUser(UserDto newUser) {
        try {
            if (!accountExist(newUser.getUsername())) {
                userRepository.save(mapper.withPassword(true).toDomain(newUser));
                return new ResponseEntity("Account added", CREATED);
            } else {
                return new ResponseEntity<String>(String.format("User %s already exist ", newUser.getUsername()), BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<String>("Error on creating user.", INTERNAL_SERVER_ERROR);
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
        return mapper.toTransaction(userRepository.findByDeletedIsFalse());
    }

    public ResponseEntity<String> checkUsername(String username) {
        try {
            if (accountExist(username))
                return new ResponseEntity<String>("Invalid Username", BAD_REQUEST);
            else
                return ResponseEntity.ok("Valid username");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on checking username", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity updateUser(UserDto userDto) {
        try {
            User userUpdate = mapper.withPassword(false).toDomain(userDto);
            userUpdate.setPassword(userRepository
                    .findById(userUpdate.getId())
                    .orElseThrow(() -> new Exception("Can't find user with id " + userUpdate.getId()))
                    .getPassword());

            userRepository.save(userUpdate);
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

    public ResponseEntity deleteUsers(List<Long> idList) {
        AtomicInteger deleteCount = new AtomicInteger(0);
        idList.forEach((id) -> {
            try {
                Optional<User> userOptional = userRepository.findById(id);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.setDeleted(true);
                    user.setDatetimeDeleted(LocalDateTime.now());
                    userRepository.save(user);
                    deleteCount.getAndIncrement();
                } else {
                    logger.error("Cannot delete user " + id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error on deleting user " + id);
            }
        });
        return new ResponseEntity(format("%d items has been deleted", deleteCount.get()), OK);
    }

    public ResponseEntity retrieveUserByUsername(String username) {
        try {
            Optional<User> userOptional = userRepository.findUserByUsername(username);
            if (userOptional.isPresent()) {
                return new ResponseEntity<UserDto>(mapper.toTransaction(userOptional.get()), OK);
            } else {
                return new ResponseEntity<String>("Can't retrieve user", BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<String>("Error on retrieving user", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity changePassword(ChangePasswordDto changePasswordDto) {
        try {
            Optional<User> userOptional = userRepository.findById(changePasswordDto.getId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                try {
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), changePasswordDto.getOldPassword()));
                } catch (Exception e) {
                    return new ResponseEntity<String>("Invalid password", BAD_REQUEST);
                }
                user.setPassword(encoder.encode(changePasswordDto.getNewPassword()));
                userRepository.save(user);
                final UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
                final String jws = jwtTokenUtil.generateAccessToken((User) userDetails);
                logger.info(userDetails.getUsername() + " has successfully authenticated");
                return ResponseEntity.ok(new AuthenticationResponse(jws));
            } else {
                return new ResponseEntity<String>("Can't find user ", BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<String>("Error on retrieving user", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity searchUser(String keyword) {
        try {
            List<User> userList = userRepository.searchUser(keyword);
            return ResponseEntity.ok(mapper.toTransaction(userList));
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<String>("Error on retrieving user", INTERNAL_SERVER_ERROR);
        }
    }
}
