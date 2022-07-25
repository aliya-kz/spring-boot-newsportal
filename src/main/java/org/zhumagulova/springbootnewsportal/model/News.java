package org.zhumagulova.springbootnewsportal.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class News implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "news_seq_gen")
    @SequenceGenerator(name = "news_seq_gen", sequenceName = "news_sequence", allocationSize = 1)

    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "news")
    @JsonManagedReference
    private Set<LocalizedNews> localizedNewsSet;

    public News(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(id, news.id);
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
