package org.zhumagulova.springbootnewsportal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhumagulova.springbootnewsportal.dao.RoleRepo;
import org.zhumagulova.springbootnewsportal.dao.UserRepo;
import org.zhumagulova.springbootnewsportal.models.Role;
import org.zhumagulova.springbootnewsportal.models.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userDao;

    private final RoleRepo roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger("NewsServiceImpl");

    @Autowired
    public UserServiceImpl(UserRepo userDao, RoleRepo roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
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
