package org.zhumagulova.springbootnewsportal.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewsNotFoundException extends Exception {

    public NewsNotFoundException(Long id) {

        super("Could not find news with id : " + id);
        log.error ("NewsNotFoundException was thrown (Exception logging)");
    }
}
