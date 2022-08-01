package org.zhumagulova.springbootnewsportal.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhumagulova.springbootnewsportal.dto.LocalizedNewsDto;
import org.zhumagulova.springbootnewsportal.exception.BatchDeleteRolledBackException;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.exception.NewsNotFoundException;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;
import org.zhumagulova.springbootnewsportal.service.NewsService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class NewsRestController {

    private final NewsService newsService;

    @Autowired
    public NewsRestController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<LocalizedNewsDto> allNews() {
        return newsService.getAllNews().stream()
                .map(LocalizedNewsDto::fromLocalizedNews)
                .collect(Collectors.toList());
    }

    @DeleteMapping
    public void batchDelete(@RequestParam String[] ids) throws BatchDeleteRolledBackException {
        long[] newsIds = Arrays.stream(ids).mapToLong(Long::parseLong).toArray();
        long[] notFoundIds = newsService.batchDelete(newsIds);
        if (notFoundIds.length > 0) {
            throw new BatchDeleteRolledBackException(notFoundIds);
        }
    }

    @PostMapping(value = "/new")
    public LocalizedNewsDto create(@Valid @RequestBody LocalizedNewsDto newsDto,
                                   @RequestParam(required = false) String newsId) throws NewsAlreadyExistsException {
        long id = StringUtils.hasText(newsId) ? Long.parseLong(newsId) : 0;
        LocalizedNews createdNews = newsService.createNews(new LocalizedNewsDto().toLocalizedNews(newsDto), id);
        return LocalizedNewsDto.fromLocalizedNews(createdNews);
    }

    @GetMapping("/{id}")
    public LocalizedNewsDto show(@PathVariable("id") long id) throws NewsNotFoundException {
        LocalizedNews news = newsService.getNewsById(id).orElseThrow(() -> new NewsNotFoundException(id));
        return LocalizedNewsDto.fromLocalizedNews(news);
    }

    @PatchMapping("/{id}")
    public LocalizedNewsDto update(@PathVariable("id") long id, @Valid @RequestBody LocalizedNewsDto newsDto) throws NewsNotFoundException {
        LocalizedNews updatedNews = newsService.updateNews(new LocalizedNewsDto().toLocalizedNews(newsDto), id);
        return LocalizedNewsDto.fromLocalizedNews(updatedNews);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) throws NewsNotFoundException {
        newsService.delete(id);
    }
}
