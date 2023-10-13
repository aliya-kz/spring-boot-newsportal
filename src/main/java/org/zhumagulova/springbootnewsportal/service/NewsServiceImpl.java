package org.zhumagulova.springbootnewsportal.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhumagulova.springbootnewsportal.api.model.LocalizedNewsRequest;
import org.zhumagulova.springbootnewsportal.dao.LocalizedNewsRepo;
import org.zhumagulova.springbootnewsportal.dao.NewsRepo;
import org.zhumagulova.springbootnewsportal.dao.NewsSourceRepo;
import org.zhumagulova.springbootnewsportal.exception.BatchDeleteRolledBackException;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.exception.NewsNotFoundException;
import org.zhumagulova.springbootnewsportal.mapper.LocalizedNewsMapper;
import org.zhumagulova.springbootnewsportal.entity.Language;
import org.zhumagulova.springbootnewsportal.entity.LocalizedNews;
import org.zhumagulova.springbootnewsportal.entity.News;
import org.zhumagulova.springbootnewsportal.entity.NewsSource;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    private final LocalizedNewsRepo localizedNewsRepo;

    private final NewsRepo newsRepo;

    private final NewsSourceRepo newsSourceRepo;

    private final LanguageService languageService;

    private final LocalizedNewsMapper localizedNewsMapper;

    private final static long RUSSIAN_LANGUAGE_ID = 2;

    private final static String DEFAULT_NEWS_SOURCE_NAME = "Internal";

    @Autowired
    public NewsServiceImpl(LocalizedNewsRepo localizedNewsRepo, NewsRepo newsRepo, NewsSourceRepo newsSourceRepo, LanguageService languageService,
                           LocalizedNewsMapper localizedNewsMapper) {
        this.localizedNewsRepo = localizedNewsRepo;
        this.newsRepo = newsRepo;
        this.newsSourceRepo = newsSourceRepo;
        this.languageService = languageService;
        this.localizedNewsMapper = localizedNewsMapper;
    }

    @Override
    @Transactional
    @Cacheable(cacheNames = "news")
    public List<LocalizedNews> getAllNews() {
        log.info("Entering getAllNews method");
        long languageId = languageService.getLanguageIdByLocale();
        return localizedNewsRepo.findAllByLanguageId(languageId);
    }

    @Override
    @Transactional
    @Cacheable(cacheNames = "news", key = "#id")
    public Optional<LocalizedNews> getNewsById(long id) {
        log.info("Entering getNewsById method");
        long languageId = languageService.getLanguageIdByLocale();
        return localizedNewsRepo.findByNewsIdAndLanguageId(id, languageId);
    }

    @Override
    @Transactional
    public LocalizedNews createNews(LocalizedNews localizedNews, long newsId) throws NewsAlreadyExistsException {
        long languageId = languageService.getLanguageIdByLocale();
        Optional<LocalizedNews> existingNews = localizedNewsRepo.findByNewsIdAndLanguageId(newsId, languageId);
        if (newsId != 0 && existingNews.isPresent()) {
            log.error("NewsAlreadyExistException was thrown at createNews method");
            throw new NewsAlreadyExistsException(newsId);
        }
        News news = new News(newsId);
        news = newsRepo.save(news);
        NewsSource newSource = newsSourceRepo.findByName(DEFAULT_NEWS_SOURCE_NAME).get();
        localizedNews.setNewsSource(newSource);
        localizedNews.setNews(news);
        Language language = languageService.getLanguageByLocale().orElseThrow(() -> new NoSuchElementException("No language found"));
        localizedNews.setLanguage(language);

        return localizedNewsRepo.save(localizedNews);
    }


    @Override
    @Transactional
    public LocalizedNews createScrapedNews(LocalizedNewsRequest request) {
        Language language = new Language(RUSSIAN_LANGUAGE_ID);
        NewsSource newSource = newsSourceRepo.findByName(request.getNewsSource()).get();
        News news = new News();
        news = newsRepo.save(news);
        LocalizedNews localizedNews = localizedNewsMapper.requestToEntity(request);
        localizedNews.setNews(news);
        localizedNews.setLanguage(language);
        localizedNews.setNewsSource(newSource);
        return localizedNewsRepo.save(localizedNews);
    }

    @Override
    @Transactional
    public LocalizedNews updateNews(@Valid LocalizedNewsRequest request, long id) throws NewsNotFoundException {
        long languageId = languageService.getLanguageIdByLocale();
        LocalizedNews databaseNews = localizedNewsRepo.findByNewsIdAndLanguageId(id, languageId)
                .orElseThrow(() -> new NewsNotFoundException(id));
        localizedNewsMapper.updateLocalizedNewsFromDto(request, databaseNews);
        localizedNewsRepo.save(databaseNews);
        return localizedNewsRepo.save(databaseNews);
    }

    @Override
    @Transactional
    public void delete(long id) throws NewsNotFoundException {
        long languageId = languageService.getLanguageIdByLocale();

        localizedNewsRepo.findByNewsIdAndLanguageId(id, languageId)
                .orElseThrow(() -> new NewsNotFoundException(id));
        localizedNewsRepo.deleteByNewsIdAndLanguageId(id, languageId);
    }

    @Override
    @Transactional
    public long[] batchDelete(long[] ids) throws BatchDeleteRolledBackException {
        long languageId = languageService.getLanguageIdByLocale();
        long[] newsThatThrowException = new long[]{};
        long temporaryId = 0;
        for (long id : ids) {
            try {
                temporaryId = id;
                localizedNewsRepo.findByNewsIdAndLanguageId(id, languageId).orElseThrow(Exception::new);
                localizedNewsRepo.deleteByNewsIdAndLanguageId(id, languageId);
            } catch (Exception e) {
                log.error("Exception was thrown by one or several news at batchDelete method");
                newsThatThrowException = new long[newsThatThrowException.length + 1];
                newsThatThrowException[newsThatThrowException.length - 1] = temporaryId;
            }
        }
        return newsThatThrowException;
    }
}

