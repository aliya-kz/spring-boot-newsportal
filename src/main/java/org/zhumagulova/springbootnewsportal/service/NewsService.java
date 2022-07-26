package org.zhumagulova.springbootnewsportal.service;


import org.springframework.transaction.annotation.Transactional;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;

import org.zhumagulova.springbootnewsportal.model.LocalizedNews;

import java.util.List;
import java.util.Optional;


public interface NewsService {

    List<LocalizedNews> getAllNews();

    Optional<LocalizedNews> getNewsById(long id);

    LocalizedNews createNews(LocalizedNews news, long newsId) throws NewsAlreadyExistsException;

    LocalizedNews updateNews(LocalizedNews news, long id);

    @Transactional
    long delete(long id);

    @Transactional
    Long[] batchDelete(Long[] ids);
}
