package org.zhumagulova.springbootnewsportal.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.NullServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String catchNewsNotFoundException(NewsNotFoundException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({NoSuchElementException.class, NullPointerException.class})
    public String catchNoSuchElementException(NoSuchElementException e) {
        return "No entity found";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler({BadCredentialsException.class})
    public String badCredentials(BadCredentialsException e) {
        return e.getMessage();
    }

    @ExceptionHandler({NewsAlreadyExistsException.class})
    public String newsAlreadyExist(NewsAlreadyExistsException e) {
        return e.getMessage();
    }
}
