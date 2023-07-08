package org.zhumagulova.springbootnewsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhumagulova.springbootnewsportal.dto.LocalizedNewsDto;
import org.zhumagulova.springbootnewsportal.exception.BatchDeleteRolledBackException;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.exception.NewsNotFoundException;
import org.zhumagulova.springbootnewsportal.mapper.LocalizedNewsMapper;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;
import org.zhumagulova.springbootnewsportal.service.NewsService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api")
@Api("News management controller")
public class NewsRestController {

    private final NewsService newsService;

    private final LocalizedNewsMapper localizedNewsMapper;

    private final static String TENGRINEWS_SOURCE_NAME = "Tengrinews";
    @Autowired
    public NewsRestController(NewsService newsService, LocalizedNewsMapper localizedNewsMapper) {
        this.newsService = newsService;
        this.localizedNewsMapper = localizedNewsMapper;
    }

    @KafkaListener(
            topics = "tengri-news")
    public void listenTengriNews(@Payload LocalizedNewsDto localizedNewsDto) throws NewsAlreadyExistsException {
        localizedNewsDto.setNewsSource(TENGRINEWS_SOURCE_NAME);
        newsService.createScrapedNews(localizedNewsDto);
        log.info("scraped news added into the database");
    }

    @GetMapping
    @ApiOperation("Getting the list of all news of current locale")
    // @Cacheable(cacheNames = "news")
    public List<LocalizedNewsDto> allNews() {
        return newsService.getAllNews().stream()
                .map(localizedNewsMapper::localizedNewsToDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping
    @ApiOperation("Batch deleting news by ids")
    public void batchDelete(@RequestParam String[] ids) throws BatchDeleteRolledBackException {
        long[] newsIds = Arrays.stream(ids).mapToLong(Long::parseLong).toArray();
        long[] notFoundIds = newsService.batchDelete(newsIds);
        if (notFoundIds.length > 0) {
            throw new BatchDeleteRolledBackException(notFoundIds);
        }
    }

    @PostMapping(value = "/new")
    @ApiOperation("Creating new news")
    public LocalizedNewsDto create(@Valid @RequestBody LocalizedNewsDto newsDto,
                                   @RequestParam(required = false) String newsId) throws NewsAlreadyExistsException {
        long id = StringUtils.hasText(newsId) ? Long.parseLong(newsId) : 0;
        LocalizedNews createdNews = newsService.createNews(localizedNewsMapper.dtoToLocalizedNews(newsDto), id);
        return localizedNewsMapper.localizedNewsToDto(createdNews);
    }

    @GetMapping("/{id}")
    @ApiOperation("Getting news by id")
    public LocalizedNewsDto getNewsById(@PathVariable("id") long id) throws NewsNotFoundException {
        LocalizedNews news = newsService.getNewsById(id).orElseThrow(() -> new NewsNotFoundException(id));
        return localizedNewsMapper.localizedNewsToDto(news);
    }

    @PatchMapping("/{id}")
    @ApiOperation("Updating news")
    public LocalizedNewsDto update(@PathVariable("id") long id, @Valid @RequestBody LocalizedNewsDto newsDto) throws NewsNotFoundException {
        LocalizedNews updatedNews = newsService.updateNews(newsDto, id);
        return localizedNewsMapper.localizedNewsToDto(updatedNews);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deleting news by id")
    public void delete(@PathVariable("id") long id) throws NewsNotFoundException {
        newsService.delete(id);
    }
}
