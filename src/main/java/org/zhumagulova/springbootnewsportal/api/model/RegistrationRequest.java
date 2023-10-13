package org.zhumagulova.springbootnewsportal.api.model;

import lombok.Data;
import lombok.Getter;
import org.zhumagulova.springbootnewsportal.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Data
public class RegistrationRequest implements Serializable {
    @Email
    private String email;

    @Size (min=4, max=12)
    private String password;

    @Size (min=4, max=12)
    private String confirmPassword;

    public User toUser () {
        User user = new User();
        user.setEmail(this.getEmail());
        user.setPassword(this.getPassword());
        return user;
    }
}
