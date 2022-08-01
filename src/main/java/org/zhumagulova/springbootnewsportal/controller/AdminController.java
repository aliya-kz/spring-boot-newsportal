package org.zhumagulova.springbootnewsportal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zhumagulova.springbootnewsportal.exception.BatchDeleteRolledBackException;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.exception.NewsNotFoundException;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;
import org.zhumagulova.springbootnewsportal.service.NewsService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final NewsService newsService;

    @Autowired
    public AdminController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public String index(Model model) {
        List<LocalizedNews> allNews = newsService.getAllNews();
        model.addAttribute("all_news", allNews);
        return "admin/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model) throws NewsNotFoundException {
        LocalizedNews news = newsService.getNewsById(id).orElseThrow(() -> new NewsNotFoundException(id));
        model.addAttribute("news", news);
        return "admin/show";
    }

    @GetMapping("/new")
    public String newNews(@ModelAttribute("news") LocalizedNews news) {
        return "admin/new";
    }


    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("news") LocalizedNews news,
                         BindingResult bindingResult, @RequestParam String newsId) throws NewsAlreadyExistsException {
        if (bindingResult.hasErrors()) {
            return "admin/error";
        }
        long id = StringUtils.hasText(newsId) ? Long.parseLong(newsId) : 0;
        newsService.createNews(news, id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) throws NewsNotFoundException {
        LocalizedNews news = newsService.getNewsById(id).orElseThrow(()->new NewsNotFoundException(id));
        model.addAttribute("news", news);
        return "admin/edit";
    }

    @PatchMapping("/{id}/edit")
    public String update(@PathVariable("id") long id, @Valid @ModelAttribute("news") LocalizedNews news,
                         BindingResult bindingResult) throws NewsNotFoundException {
        if (bindingResult.hasErrors()) {
            return "admin/edit";
        }
        newsService.updateNews(news, id);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteOneNews(@PathVariable("id") long id) throws NewsNotFoundException {
        newsService.delete(id);
        return "redirect:/admin";
    }

    @DeleteMapping
    public String delete(@RequestParam String[] ids) throws BatchDeleteRolledBackException {
        long[] newsIds = Arrays.stream(ids).mapToLong(Long::parseLong).toArray();
        newsService.batchDelete(newsIds);
        return "redirect:/admin";
    }

}
