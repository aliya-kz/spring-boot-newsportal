package org.zhumagulova.springbootnewsportal.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.exception.NewsNotFoundException;
import org.zhumagulova.springbootnewsportal.model.Language;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Sql(value = "classpath:test-sql/test-data.sql", executionPhase = Sql.ExecutionPhase
        .BEFORE_TEST_METHOD)
class NewsServiceImplTest extends BaseIntegrationTest {

    private final static long EXISTING_LANGUAGE_ID = 1;
    private final static long EXISTING_LOCALIZED_NEWS_ID = 1;
    private final static long NON_EXISTING_LOCALIZED_NEWS_ID = 17;
    private final static long LOCALIZED_NEWS_OF_SINGLE_LANGUAGE_LIST_SIZE = 2;
    private final static long EXISTING_NEWS_ID = 1;
    private final static long DEFAULT_NEWS_ID = 0;
    private final static String TITLE = "Test title";
    private final static String BRIEF = "Test brief";
    private final static String CONTENT = "Test content";


    @Autowired
    private NewsService newsService;

    @Test
    void getLocalizedNewsById_NewsExist_True() {
        Optional<LocalizedNews> news = newsService.getNewsById(EXISTING_LOCALIZED_NEWS_ID);
        Assertions.assertNotNull(news.get());
    }

    @Test
    void getLocalizedNewsById_NewsNotExist_EmptyOptional() {
        assertTrue(newsService.getNewsById(NON_EXISTING_LOCALIZED_NEWS_ID).isEmpty());
    }

    @Test
    void getAllNews_NewsSizeEqualsFour_True() {
        List<LocalizedNews> list = newsService.getAllNews();
        assertEquals(LOCALIZED_NEWS_OF_SINGLE_LANGUAGE_LIST_SIZE, list.size());
    }

    @Test
    void getAllNews_NewsSizeEqualsSeven_False() {
        List<LocalizedNews> list = newsService.getAllNews();
        assertNotEquals(7, list.size());
    }

    @Test
    void createNews_Success_True() throws NewsAlreadyExistsException {
        Language language = new Language(EXISTING_LANGUAGE_ID);
        LocalizedNews localizedNews = LocalizedNews.builder()
                .title(TITLE)
                .brief(BRIEF)
                .content(CONTENT)
                .date(LocalDate.now())
                .language(language)
                .build();

        LocalizedNews createdNews = newsService.createNews(localizedNews, DEFAULT_NEWS_ID);
        assertEquals(BRIEF, createdNews.getBrief());
    }

    @Test
    void createNews_NewsWithIdAndLanguageAlreadyExist_ThrowsException() throws DataIntegrityViolationException {
        Language language = new Language(EXISTING_LANGUAGE_ID);
        LocalizedNews localizedNews = LocalizedNews.builder()
                .title(TITLE)
                .brief(BRIEF)
                .content(CONTENT)
                .date(LocalDate.now())
                .language(language)
                .build();

        assertThrows(NewsAlreadyExistsException.class, () -> newsService.createNews(localizedNews, EXISTING_NEWS_ID));
    }

    @Test
    void updateNews_Success_True() throws NewsNotFoundException {
        Language language = new Language(EXISTING_LANGUAGE_ID);
        LocalizedNews localizedNews = LocalizedNews.builder()
                .title(TITLE)
                .brief(BRIEF)
                .content(CONTENT)
                .date(LocalDate.now())
                .language(language)
                .build();

        newsService.updateNews(localizedNews, EXISTING_NEWS_ID);

        LocalizedNews databaseNews = newsService.getNewsById(EXISTING_NEWS_ID).get();
        assertEquals(TITLE, databaseNews.getTitle());
    }

    @Test
    void updateNews_NewsWithSuchIdNotExist_ThrowsException() {
        Language language = new Language(EXISTING_LANGUAGE_ID);
        LocalizedNews localizedNews = LocalizedNews.builder()
                .title(TITLE)
                .brief(BRIEF)
                .content(CONTENT)
                .date(LocalDate.now())
                .language(language)
                .build();

        assertThrows(NewsNotFoundException.class, () -> newsService.updateNews(localizedNews, NON_EXISTING_LOCALIZED_NEWS_ID));
    }

    @Test
    void deleteNews_Success_True() throws NewsNotFoundException {
        List<LocalizedNews> list = newsService.getAllNews();
        int initialSize = list.size();

        newsService.delete(1);

        List<LocalizedNews> listAfterDeletion = newsService.getAllNews();
        int sizeAfterDeletion = listAfterDeletion.size();
        assertEquals(initialSize, sizeAfterDeletion + 1);
    }

    @Test
    void deleteNews_NewsNotExist_ThrowsException() {
        assertThrows(NewsNotFoundException.class, () -> newsService.delete(NON_EXISTING_LOCALIZED_NEWS_ID));
    }
}