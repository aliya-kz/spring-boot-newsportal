package org.zhumagulova.springbootnewsportal.controller.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zhumagulova.springbootnewsportal.exception.BatchDeleteRolledBackException;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.exception.NewsNotFoundException;
import org.zhumagulova.springbootnewsportal.model.response.CustomResponse;
import org.zhumagulova.springbootnewsportal.model.response.MethodArgumentNotValidResponse;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MethodArgumentNotValidResponse handleValidationExceptions(MethodArgumentNotValidException exception) {
        return new MethodArgumentNotValidResponse(exception);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler({NewsNotFoundException.class})
    public CustomResponse catchNewsNotFoundException(NewsNotFoundException exception) {
        return new CustomResponse(exception);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoSuchElementException.class})
    public CustomResponse catchNoSuchElementException(NoSuchElementException exception) {
        return new CustomResponse(exception);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({BadCredentialsException.class})
    public CustomResponse badCredentials(BadCredentialsException exception) {
        return new CustomResponse(exception);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({NewsAlreadyExistsException.class})
    public CustomResponse newsAlreadyExist(NewsAlreadyExistsException exception) {
        return new CustomResponse(exception);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler({BatchDeleteRolledBackException.class})
    public CustomResponse catchBatchDeleteException(BatchDeleteRolledBackException exception) {
        return new CustomResponse(exception);
    }
}
