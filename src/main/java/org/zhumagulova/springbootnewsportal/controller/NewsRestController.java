package org.zhumagulova.springbootnewsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api ("News management controller")
public class NewsRestController {

    private final NewsService newsService;

    @Autowired
    public NewsRestController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    @ApiOperation("Getting the list of all news of current locale")
    public List<LocalizedNewsDto> allNews() {
        return newsService.getAllNews().stream()
                .map(LocalizedNewsMapper.INSTANCE::localizedNewsToDto)
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
        LocalizedNews createdNews = newsService.createNews(LocalizedNewsMapper.INSTANCE.dtoToLocalizedNews(newsDto), id);
        return LocalizedNewsMapper.INSTANCE.localizedNewsToDto(createdNews);
    }

    @GetMapping("/{id}")
    @ApiOperation("Getting news by id")
    public LocalizedNewsDto show(@PathVariable("id") long id) throws NewsNotFoundException {
        LocalizedNews news = newsService.getNewsById(id).orElseThrow(() -> new NewsNotFoundException(id));
        return LocalizedNewsMapper.INSTANCE.localizedNewsToDto(news);
    }

    @PatchMapping("/{id}")
    @ApiOperation("Updating news")
    public LocalizedNewsDto update(@PathVariable("id") long id, @Valid @RequestBody LocalizedNewsDto newsDto) throws NewsNotFoundException {
        LocalizedNews updatedNews = newsService.updateNews(newsDto, id);
        return LocalizedNewsMapper.INSTANCE.localizedNewsToDto(updatedNews);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deleting news by id")
    public void delete(@PathVariable("id") long id) throws NewsNotFoundException {
        newsService.delete(id);
    }
}