package org.zhumagulova.springbootnewsportal.models;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name="languages")
@Data
@NoArgsConstructor
public class Language implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 2, max = 2, message = "Language code should be 2 characters")
    private String code;

    @OneToMany(mappedBy="language")
    private Set<LocalizedNews> localizedNewsSet;

    public Language(long existingLanguageId) {
        this.id = existingLanguageId;
    }
}
