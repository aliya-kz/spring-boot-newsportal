package org.zhumagulova.springbootnewsportal.exception;

import javax.naming.AuthenticationException;

public class UserNotFoundException extends AuthenticationException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
