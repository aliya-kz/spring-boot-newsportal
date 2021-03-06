package org.zhumagulova.springbootnewsportal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zhumagulova.springbootnewsportal.model.Language;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;
import org.zhumagulova.springbootnewsportal.service.NewsService;

import java.time.LocalDate;
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
@WithMockUser(username = "admin@admin.com", password = "12345", roles = {"ADMIN"})
class AdminControllerTest {

    @Mock
    private NewsService newsService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    private final static String TITLE = "Test title";
    private final static String BRIEF = "Test brief";
    private final static String CONTENT = "Test content";
    private final static long ID = 1;

    LocalizedNews mockNews = LocalizedNews.builder()
            .title(TITLE)
            .brief(BRIEF)
            .content(CONTENT)
            .date(LocalDate.now())
            .language(new Language(ID))
            .build();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void testLoadIndexPage() throws Exception {
        List<LocalizedNews> newsList = new ArrayList<>();
        newsList.add(mockNews);
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
        Optional <LocalizedNews> localizedNews = Optional.of(mockNews);
        when(newsService.getNewsById(id).get()).thenReturn(mockNews);

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