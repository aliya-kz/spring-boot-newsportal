package org.zhumagulova.springbootnewsportal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zhumagulova.springbootnewsportal.models.LocalizedNews;
import org.zhumagulova.springbootnewsportal.service.NewsService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public String index(Model model) {
        List<LocalizedNews> allNews = newsService.getAllNews();
        model.addAttribute("all_news", allNews);
        return "news/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        LocalizedNews news = newsService.getNewsById(id).get();
        model.addAttribute("news", news);
        return "news/show";
    }

    @ExceptionHandler(value = {NoSuchElementException.class, NullPointerException.class})
    public String noSuchElement(Model model) {
        model.addAttribute("error_msg", "no_element");
        return "error";
    }
}
