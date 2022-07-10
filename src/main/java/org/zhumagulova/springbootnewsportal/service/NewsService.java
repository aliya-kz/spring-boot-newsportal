package org.zhumagulova.springbootnewsportal.service;


import org.springframework.transaction.annotation.Transactional;
import org.zhumagulova.springbootnewsportal.exceptions.NewsAlreadyExistsException;

import org.zhumagulova.springbootnewsportal.models.LocalizedNews;

import java.util.List;
import java.util.Optional;


public interface NewsService {

    List<LocalizedNews> getAllNews();

    Optional<LocalizedNews> getNewsById(long id);

    LocalizedNews createNews(LocalizedNews news, long newsId) throws NewsAlreadyExistsException;

    void updateNews(LocalizedNews news, long id);

    @Transactional
    void delete(long id);

    @Transactional
    void deleteSeveral(Long[] ids);
}
