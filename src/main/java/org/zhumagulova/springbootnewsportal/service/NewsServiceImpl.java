package org.zhumagulova.springbootnewsportal.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhumagulova.springbootnewsportal.dao.LocalizedNewsRepo;
import org.zhumagulova.springbootnewsportal.dao.NewsRepo;
import org.zhumagulova.springbootnewsportal.exceptions.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.models.Language;
import org.zhumagulova.springbootnewsportal.models.LocalizedNews;
import org.zhumagulova.springbootnewsportal.models.News;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class NewsServiceImpl implements NewsService {

    private static final Logger logger = LoggerFactory.getLogger("NewsServiceImpl");

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
        return localizedNewsRepo.findAllByLanguage_Id(languageId);
    }

    @Override
    @Transactional
    public Optional<LocalizedNews> getNewsById(long id) {
        long languageId = languageService.getLanguageIdByLocale();
        return localizedNewsRepo.findByNews_IdAndLanguage_Id(id, languageId);
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
    public void updateNews(LocalizedNews localizedNews, long id) {
        long languageId = languageService.getLanguageIdByLocale();
        LocalizedNews databaseNews = localizedNewsRepo.findByNews_IdAndLanguage_Id(id, languageId).get();        databaseNews.setTitle(localizedNews.getTitle());
        databaseNews.setBrief(localizedNews.getBrief());
        databaseNews.setContent(localizedNews.getContent());
        databaseNews.setDate(localizedNews.getDate());
        localizedNewsRepo.save(databaseNews);
    }

    @Override
    @Transactional
    public void delete(long id) {
        long languageId = languageService.getLanguageIdByLocale();
        localizedNewsRepo.deleteByNews_IdAndLanguage_Id(id, languageId);
    }


    @Override
    @Transactional
    public void deleteSeveral(Long[] ids) {
        long languageId = languageService.getLanguageIdByLocale();
        Arrays.stream(ids).forEach(id -> localizedNewsRepo.deleteByNews_IdAndLanguage_Id(id, languageId));
    }
}
