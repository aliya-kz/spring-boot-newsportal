package org.zhumagulova.springbootnewsportal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhumagulova.springbootnewsportal.dao.UserRepo;
import org.zhumagulova.springbootnewsportal.entity.Role;
import org.zhumagulova.springbootnewsportal.entity.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userDao;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L));
        user.setRoles(roles);
        userDao.save(user);
    }

    @Transactional
    @Override
    public Optional<User> findUserByEmail(String email) {
        return userDao.findByEmail(email);
    }


    @Transactional
    @Override
    public boolean userAuthentificated(String email, String password) {
        String databasePassword = userDao.findPasswordByEmail(email);
        return passwordEncoder.matches(password, databasePassword);
    }
}
