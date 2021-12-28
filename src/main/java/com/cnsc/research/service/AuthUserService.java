package com.cnsc.research.service;

import com.cnsc.research.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class AuthUserService implements UserDetailsService {
    private final UserRepository repository;

    @Autowired
    public AuthUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsernameAndDeletedIsFalse(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(format("%s not found", username))
                );
    }
}
