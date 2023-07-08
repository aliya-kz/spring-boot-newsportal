package org.zhumagulova.springbootnewsportal.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "news_source")
@Data
@NoArgsConstructor
public class NewsSource implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "news_source_seq_gen")
    @SequenceGenerator(name = "news_source_seq_gen", sequenceName = "news_source_sequence", allocationSize = 1)
    private long id;

    @Column(name = "name", unique = true)
    @Size(min = 2, max = 50, message = "Source name should be from 2 to 50 characters")
    private String name;

    @OneToMany(mappedBy = "newsSource")
    private Set<LocalizedNews> localizedNews;

    public NewsSource(String name) {
        this.name = name;
    }

}
