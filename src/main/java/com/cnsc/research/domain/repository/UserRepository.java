package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String userName);

    Optional<User> findByUsernameAndDeletedIsFalse(String username);

    List<User> findByDeletedIsFalse();

}
