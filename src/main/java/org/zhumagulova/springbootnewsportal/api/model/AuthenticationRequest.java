package org.zhumagulova.springbootnewsportal.api.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Data
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {
    private String email;
    private String password;
}
