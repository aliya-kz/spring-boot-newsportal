package org.zhumagulova.springbootnewsportal.api.model.response;

import lombok.Data;

@Data
public class CustomResponse {

    private String exception;
    private String reason;

    public CustomResponse(Throwable throwable) {
        exception = throwable.getClass().getSimpleName();
        reason = throwable.getMessage();
    }
}
