package org.zhumagulova.springbootnewsportal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zhumagulova.springbootnewsportal.model.News;

@Repository
public interface NewsRepo extends JpaRepository<News, Long> {

}
