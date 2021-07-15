package com.cnsc.research.api;

import com.cnsc.research.configuration.security.JwtTokenUtil;
import com.cnsc.research.domain.model.User;
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

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AuthUserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final Logger logger;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    AuthUserService userService,
                                    JwtTokenUtil jwtTokenUtil,
                                    Logger logger) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.logger = logger;
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
        logger.info(userDetails.getUsername() +" has successfully authenticated");
        return ResponseEntity.ok(new AuthenticationResponse(jws));
    }

}
