package org.zhumagulova.springbootnewsportal.exception;

public class NewsAlreadyExistsException extends Exception{

private final String message = "news_exist";

    public NewsAlreadyExistsException(String message) {
        super(message);
    }

    public NewsAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
