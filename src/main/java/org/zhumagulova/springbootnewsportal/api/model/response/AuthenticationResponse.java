package org.zhumagulova.springbootnewsportal.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String email;
    private String token;
}
