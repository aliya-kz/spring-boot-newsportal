package org.zhumagulova.springbootnewsportal.exception;

public class NewsNotFoundException extends Exception {

    public NewsNotFoundException(Long id) {
        super("Could not find news with id : " + id);
    }
}
