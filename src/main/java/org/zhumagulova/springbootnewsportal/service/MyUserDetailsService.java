package org.zhumagulova.springbootnewsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zhumagulova.springbootnewsportal.dao.UserRepo;
import org.zhumagulova.springbootnewsportal.models.MyUserDetails;
import org.zhumagulova.springbootnewsportal.models.User;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found with such email: " + email));
        return user.map(MyUserDetails::new).get();
    }
}

