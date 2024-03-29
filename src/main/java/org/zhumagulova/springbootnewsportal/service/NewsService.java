package org.zhumagulova.springbootnewsportal.service;


import org.springframework.transaction.annotation.Transactional;
import org.zhumagulova.springbootnewsportal.dto.LocalizedNewsDto;
import org.zhumagulova.springbootnewsportal.exception.BatchDeleteRolledBackException;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;

import org.zhumagulova.springbootnewsportal.exception.NewsNotFoundException;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;

import java.util.List;
import java.util.Optional;


public interface NewsService {

    List<LocalizedNews> getAllNews();

    Optional<LocalizedNews> getNewsById(long id);

    LocalizedNews createNews(LocalizedNews news, long newsId) throws NewsAlreadyExistsException;

    @Transactional
    LocalizedNews updateNews(LocalizedNewsDto localizedNewsDto, long id) throws NewsNotFoundException;

    @Transactional
    void delete(long id) throws NewsNotFoundException;

    @Transactional
    long[] batchDelete(long[] ids) throws BatchDeleteRolledBackException;
}
