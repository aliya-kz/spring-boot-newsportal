package org.zhumagulova.springbootnewsportal.passwordEncoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordEncoder implements PasswordEncoder {

    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder().encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder().matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        System.out.println(passwordEncoder().matches("12345", "$2a$10$3TLjSfivla69omi8M0bAGeu0QNpaSe/G5Y.IMOnasUlqYw9yrnuwC"));
    }
}
