package org.zhumagulova.springbootnewsportal.service;

import org.zhumagulova.springbootnewsportal.models.User;

import javax.validation.Valid;
import java.util.Optional;

public interface UserService {

    void save(@Valid User user);

    Optional<User> findUserByEmail(String email);

    boolean userAuthentificated (String email, String password);
}
