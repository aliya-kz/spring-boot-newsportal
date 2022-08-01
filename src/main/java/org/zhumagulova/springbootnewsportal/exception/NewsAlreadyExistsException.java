package org.zhumagulova.springbootnewsportal.exception;

public class NewsAlreadyExistsException extends Exception{

    public NewsAlreadyExistsException(long id) {
        super("News with id: " + id + " already exist");
    }

}
