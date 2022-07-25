package org.zhumagulova.springbootnewsportal.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhumagulova.springbootnewsportal.dao.LocalizedNewsRepo;
import org.zhumagulova.springbootnewsportal.dao.NewsRepo;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.model.Language;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;
import org.zhumagulova.springbootnewsportal.model.News;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    private final LocalizedNewsRepo localizedNewsRepo;

    private final NewsRepo newsRepo;

    private final LanguageService languageService;

    @Autowired
    public NewsServiceImpl(LocalizedNewsRepo localizedNewsRepo, NewsRepo newsRepo, LanguageService languageService) {
        this.localizedNewsRepo = localizedNewsRepo;
        this.newsRepo = newsRepo;
        this.languageService = languageService;
    }

    @Override
    @Transactional
    public List<LocalizedNews> getAllNews() {
        long languageId = languageService.getLanguageIdByLocale();
        return localizedNewsRepo.findAllByLanguageId(languageId);
    }

    @Override
    @Transactional
    public Optional<LocalizedNews> getNewsById(long id) {
        long languageId = languageService.getLanguageIdByLocale();
        return localizedNewsRepo.findByNewsIdAndLanguageId(id, languageId);
    }

    @Override
    @Transactional
    public LocalizedNews createNews(LocalizedNews localizedNews, long newsId) throws NewsAlreadyExistsException {
        News news = new News(newsId);
        if (newsId == 0) {
            news = newsRepo.save(news);
        }
        localizedNews.setNews(news);
        Language language = languageService.getLanguageByLocale().get();
        localizedNews.setLanguage(language);
        try {
            return localizedNewsRepo.save(localizedNews);
        } catch (Exception e) {
            throw new NewsAlreadyExistsException("news_exist");
        }
    }

    @Override
    @Transactional
    public LocalizedNews updateNews(LocalizedNews localizedNews, long id) {
        long languageId = languageService.getLanguageIdByLocale();
        LocalizedNews databaseNews = localizedNewsRepo.findByNewsIdAndLanguageId(id, languageId).get();        databaseNews.setTitle(localizedNews.getTitle());
        databaseNews.setBrief(localizedNews.getBrief());
        databaseNews.setContent(localizedNews.getContent());
        databaseNews.setDate(localizedNews.getDate());
        return localizedNewsRepo.save(databaseNews);
    }

    @Override
    @Transactional
    public void delete(long id) {
        long languageId = languageService.getLanguageIdByLocale();
        localizedNewsRepo.deleteByNewsIdAndLanguageId(id, languageId);
    }


    @Override
    @Transactional
    public void batchDelete(Long[] ids) {
        long languageId = languageService.getLanguageIdByLocale();
        Arrays.stream(ids).forEach(id -> localizedNewsRepo.deleteByNewsIdAndLanguageId(id, languageId));
    }
}
