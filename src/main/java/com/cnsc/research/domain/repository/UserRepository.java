package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String userName);

    Optional<User> findByUsernameAndDeletedIsFalse(String username);

    List<User> findByDeletedIsFalse();

    long countByDeletedIsFalse();


    @Query("select u from User u where upper(u.username) like upper(concat('%', ?1, '%')) or upper(u.name) like upper(concat('%', ?1, '%')) and u.deleted = false")
    List<User> searchUser(String keyword);

}
