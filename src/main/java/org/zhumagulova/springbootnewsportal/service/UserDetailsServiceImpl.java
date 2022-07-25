package org.zhumagulova.springbootnewsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zhumagulova.springbootnewsportal.dao.UserRepo;
import org.zhumagulova.springbootnewsportal.model.UserDetailsImpl;
import org.zhumagulova.springbootnewsportal.model.User;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepo userDao;

    @Autowired
    public UserDetailsServiceImpl(UserRepo userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found with such email: " + email));
        return user.map(UserDetailsImpl::new).get();
    }
}

