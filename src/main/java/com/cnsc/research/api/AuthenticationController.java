package com.cnsc.research.api;

import com.cnsc.research.configuration.security.JwtTokenUtil;
import com.cnsc.research.domain.model.AuthenticationDto;
import com.cnsc.research.domain.model.User;
import com.cnsc.research.domain.repository.UserRepository;
import com.cnsc.research.domain.transaction.AuthenticationRequest;
import com.cnsc.research.domain.transaction.AuthenticationResponse;
import com.cnsc.research.service.AuthUserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AuthUserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final Logger logger;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    AuthUserService userService,
                                    JwtTokenUtil jwtTokenUtil,
                                    Logger logger, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.logger = logger;
        this.userRepository = userRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException exception) {
            throw new Exception("Incorrect username or password");
        }
        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
        final String jws = jwtTokenUtil.generateAccessToken((User) userDetails);
        logger.info(userDetails.getUsername() + " has successfully authenticated");
        return ResponseEntity.ok(new AuthenticationResponse(jws));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationDto authenticationDto) {
        try {
            Optional<User> userOptional = userRepository.findById(authenticationDto.getId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), authenticationDto.getPassword()));
                return new ResponseEntity("Authentication Success", OK);
            } else {
                return new ResponseEntity<String>("Can't find user ", BAD_REQUEST);
            }
        } catch (BadCredentialsException e) {
            return new ResponseEntity<String>("Wrong password", BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<String>("Error on resetting password", INTERNAL_SERVER_ERROR);
        }
    }

}
