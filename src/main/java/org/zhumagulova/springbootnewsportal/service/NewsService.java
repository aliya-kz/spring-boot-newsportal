package org.zhumagulova.springbootnewsportal.service;


import org.springframework.transaction.annotation.Transactional;
import org.zhumagulova.springbootnewsportal.api.model.LocalizedNewsRequest;
import org.zhumagulova.springbootnewsportal.exception.BatchDeleteRolledBackException;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;

import org.zhumagulova.springbootnewsportal.exception.NewsNotFoundException;
import org.zhumagulova.springbootnewsportal.entity.LocalizedNews;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


public interface NewsService {

    List<LocalizedNews> getAllNews();

    Optional<LocalizedNews> getNewsById(long id);

    @Transactional
    LocalizedNews createNews(LocalizedNews news, long newsId) throws NewsAlreadyExistsException;

    LocalizedNews createScrapedNews(LocalizedNewsRequest request);
    @Transactional
    LocalizedNews updateNews(@Valid LocalizedNewsRequest localizedNewsDto, long id) throws NewsNotFoundException;

    @Transactional
    void delete(long id) throws NewsNotFoundException;

    @Transactional
    long[] batchDelete(long[] ids) throws BatchDeleteRolledBackException;
}
