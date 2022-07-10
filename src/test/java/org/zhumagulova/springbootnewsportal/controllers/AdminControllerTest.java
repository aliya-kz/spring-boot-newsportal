package org.zhumagulova.springbootnewsportal.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zhumagulova.springbootnewsportal.models.LocalizedNews;
import org.zhumagulova.springbootnewsportal.service.NewsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class AdminControllerTest {

    @Mock
    private NewsService newsService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void testLoadIndexPage() throws Exception {
        List<LocalizedNews> newsList = new ArrayList<>();
        newsList.add(new LocalizedNews());
        newsList.add(new LocalizedNews());

        when(newsService.getAllNews()).thenReturn(newsList);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"))
                .andExpect(model().attribute("all_news", hasSize(2)));
    }

    @Test
    void testLoadShowPage() throws Exception {
        long id = 1;
        Optional <LocalizedNews> localizedNews = Optional.of(new LocalizedNews());

        when(newsService.getNewsById(id)).thenReturn(localizedNews);

        mockMvc.perform(get("/admin/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/show"))
                .andExpect(model().attribute("news", instanceOf(LocalizedNews.class)));
    }

    @Test
    void testLoadNewNewsPage() throws Exception {
        mockMvc.perform(get("/admin/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/new"))
                .andExpect(model().attribute("news", instanceOf(LocalizedNews.class)));
    }


    @Test
    void testCreateNews() throws Exception {
        mockMvc.perform(post("/admin/new")
                        .param("newsId", String.valueOf(1))
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }


}