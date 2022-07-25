package org.zhumagulova.springbootnewsportal.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Data
@NoArgsConstructor
public class AuthenticationRequestDto implements Serializable {
    private String email;
    private String password;
}
