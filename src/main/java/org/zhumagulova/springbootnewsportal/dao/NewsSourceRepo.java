package org.zhumagulova.springbootnewsportal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zhumagulova.springbootnewsportal.model.NewsSource;

import java.util.Optional;

public interface NewsSourceRepo extends JpaRepository<NewsSource, Long> {
    Optional<NewsSource> findByName(String name);

}
