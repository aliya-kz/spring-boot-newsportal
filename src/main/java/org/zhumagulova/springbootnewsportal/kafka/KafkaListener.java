package org.zhumagulova.springbootnewsportal.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.zhumagulova.springbootnewsportal.api.model.LocalizedNewsRequest;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.service.NewsService;


@Slf4j
@Component
public class KafkaListener {

    private final NewsService newsService;
    private final static String TENGRINEWS_SOURCE_NAME = "Tengrinews";

    public KafkaListener(NewsService newsService) {
        this.newsService = newsService;
    }

    @org.springframework.kafka.annotation.KafkaListener(
            topics = "tengri-news")
    public void listenTengriNews(@Payload LocalizedNewsRequest request) throws NewsAlreadyExistsException {
        request.setNewsSource(TENGRINEWS_SOURCE_NAME);
        newsService.createScrapedNews(request);
        log.info("scraped news added into the database");
    }
}
