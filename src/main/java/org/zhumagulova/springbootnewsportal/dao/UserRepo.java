package org.zhumagulova.springbootnewsportal.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.zhumagulova.springbootnewsportal.entity.User;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("select u.password from User u where u.email = :email")
    String findPasswordByEmail(String email);
}
