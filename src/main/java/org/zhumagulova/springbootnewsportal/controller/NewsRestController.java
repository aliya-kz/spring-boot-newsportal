package org.zhumagulova.springbootnewsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhumagulova.springbootnewsportal.api.model.LocalizedNewsRequest;
import org.zhumagulova.springbootnewsportal.api.model.LocalizedNewsResponse;
import org.zhumagulova.springbootnewsportal.exception.BatchDeleteRolledBackException;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.exception.NewsNotFoundException;
import org.zhumagulova.springbootnewsportal.mapper.LocalizedNewsMapper;
import org.zhumagulova.springbootnewsportal.entity.LocalizedNews;
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

    @Autowired
    public NewsRestController(NewsService newsService, LocalizedNewsMapper localizedNewsMapper) {
        this.newsService = newsService;
        this.localizedNewsMapper = localizedNewsMapper;
    }

    @GetMapping
    @ApiOperation("Getting the list of all news of current locale")
    // @Cacheable(cacheNames = "news")
    public List<LocalizedNewsResponse> allNews() {
        return newsService.getAllNews().stream()
                .map(localizedNewsMapper::entityToResponse)
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
    public LocalizedNewsResponse create(@Valid @RequestBody LocalizedNewsRequest request,
                                   @RequestParam(required = false) String newsId) throws NewsAlreadyExistsException {
        long id = StringUtils.hasText(newsId) ? Long.parseLong(newsId) : 0;
        LocalizedNews createdNews = newsService.createNews(localizedNewsMapper.requestToEntity(request), id);
        return localizedNewsMapper.entityToResponse(createdNews);
    }

    @GetMapping("/{id}")
    @ApiOperation("Getting news by id")
    public LocalizedNewsResponse getNewsById(@PathVariable("id") long id) throws NewsNotFoundException {
        LocalizedNews news = newsService.getNewsById(id).orElseThrow(() -> new NewsNotFoundException(id));
        return localizedNewsMapper.entityToResponse(news);
    }

    @PatchMapping("/{id}")
    @ApiOperation("Updating news")
    public LocalizedNewsResponse update(@PathVariable("id") long id, @Valid @RequestBody LocalizedNewsRequest request) throws NewsNotFoundException {
        LocalizedNews updatedNews = newsService.updateNews(request, id);
        return localizedNewsMapper.entityToResponse(updatedNews);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deleting news by id")
    public void delete(@PathVariable("id") long id) throws NewsNotFoundException {
        newsService.delete(id);
    }
}
