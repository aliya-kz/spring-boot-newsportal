package org.zhumagulova.springbootnewsportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name="users")
@NamedQuery(
        name = "selectUserByEmail",
        query = "select u from User u where u.email = :email"
)
@Data
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="users_seq_gen")
    @SequenceGenerator(name="users_seq_gen", sequenceName="users_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "email")
    @Email
    @NotEmpty
    private String email;

    @JsonIgnore
    @NotEmpty
    @Column(name = "password")
    private String password;

    @Transient
    private String confirmPassword;

    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
