package org.zhumagulova.springbootnewsportal.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.exception.NewsNotFoundException;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;
import org.zhumagulova.springbootnewsportal.service.NewsService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class AdminRestController {

    private final NewsService newsService;

    @Autowired
    public AdminRestController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<LocalizedNews> allNews() {
        return newsService.getAllNews();
    }

    @GetMapping("/{id}")
    public LocalizedNews show(@PathVariable("id") long id) throws NewsNotFoundException {
        return newsService.getNewsById(id).orElseThrow(() -> new NewsNotFoundException(id));
    }

    @GetMapping("/new")
    public LocalizedNews newNews() {
        return new LocalizedNews();
    }

    @PostMapping(value = "/new")
    public LocalizedNews create(@Valid @RequestBody LocalizedNews news,
                                @RequestParam String newsId) throws NewsAlreadyExistsException {
        long id = StringUtils.hasText(newsId) ? Long.parseLong(newsId) : 0;
        return newsService.createNews(news, id);
    }

    @GetMapping("/{id}/edit")
    public LocalizedNews edit(@PathVariable("id") long id) throws NewsNotFoundException {
        return newsService.getNewsById(id).orElseThrow(() -> new NewsNotFoundException(id));
    }

    @PatchMapping("/{id}/edit")
    public LocalizedNews update(@PathVariable("id") long id, @Valid @RequestBody LocalizedNews news) {
        return newsService.updateNews(news, id);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        newsService.delete(id);
    }

    @DeleteMapping
    public void batchDelete(@RequestParam String[] ids) {
        Long[] newsIds = Arrays.stream(ids).map(id -> Long.parseLong(id)).toArray(Long[]::new);
        newsService.batchDelete(newsIds);
    }
}
