package org.zhumagulova.springbootnewsportal.controller.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.exception.NewsNotFoundException;
import org.zhumagulova.springbootnewsportal.model.Language;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;
import org.zhumagulova.springbootnewsportal.service.NewsService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "admin@admin.com", password = "12345", roles = {"ADMIN"})
class AdminRestControllerTest {

    private final static long ID = 1;
    private final static int LOCALIZED_NEWS_OF_SINGLE_LANGUAGE_LIST_SIZE = 2;
    private final static String TITLE = "Test title";
    private final static String BRIEF = "Test brief";
    private final static String CONTENT = "Test content";

    @MockBean
    private NewsService newsService;

    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

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
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void showAllNews_success() throws Exception {
        List<LocalizedNews> allNews = new ArrayList<>(Arrays.asList(new LocalizedNews(), mockNews));

        when(newsService.getAllNews()).thenReturn(allNews);

        mockMvc.perform(get("/api")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(LOCALIZED_NEWS_OF_SINGLE_LANGUAGE_LIST_SIZE)))
                .andExpect(jsonPath("$[1].title").value(TITLE));
    }

    @Test
    void showNewsById_success() throws Exception {
        when(newsService.getNewsById(ID)).thenReturn(Optional.of(mockNews));
        mockMvc.perform(get("/api/{id}", ID)).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(mockNews.getTitle()))
                .andExpect(jsonPath("$.brief").value(mockNews.getBrief()))
                .andExpect(jsonPath("$.content").value(mockNews.getContent()))
                .andDo(print());
    }

    @Test
    void showNewsById_NoNewsFound_ThrowNewsNotFoundException() throws Exception {
        when(newsService.getNewsById(ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/{id}", ID))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof NewsNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find news with id : " + ID,
                                result.getResolvedException().getMessage()))
                .andDo(print());
    }

    @Test
    void showGetNewNews_NewsWithNullValues_Success() throws Exception {
        mockMvc.perform(get("/api/new")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }


    @Test
    void createLocalizedNews_Success() throws Exception {
        when(newsService.createNews(mockNews, ID)).thenReturn(mockNews);

        mockMvc.perform(post("/api/new"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(mockNews.getTitle()))
                .andDo(print());
    }

    @Test
    void createLocalizedNews_NewsWithIdExist_ThrowsException() throws Exception {
        when(newsService.createNews(mockNews, ID)).thenThrow(new NewsAlreadyExistsException(ID));

        mockMvc.perform(post("/api/new"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("\"News with id 1 already exist\""))
                .andDo(print());
    }

    @Test
    void updateLocalizedNews_Success() throws Exception {
        when(newsService.updateNews(mockNews, ID)).thenReturn(mockNews);

        mockMvc.perform(post("/api/{id}/edit", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(mockNews.getTitle()))
                .andDo(print());
    }

    @Test
    void updateLocalizedNews_NewsWithIdNotExist_ThrowsException() throws Exception {
        when(newsService.updateNews(mockNews, ID)).thenThrow(new NullPointerException());

        mockMvc.perform(post("/api/{id}/edit", ID))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("\"No entity found\""))
                .andDo(print());
    }

    @Test
    void deleteLocalizedNews_Success() throws Exception {
        when(newsService.delete(ID)).thenReturn(1L);

        mockMvc.perform(delete("/api/{id}/edit", ID))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteLocalizedNews_NewsWithIdNotExist_ThrowsException() throws Exception {
        when(newsService.delete(ID)).thenReturn(0L);

        mockMvc.perform(delete("/api/{id}/edit", ID))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    void deleteBatch_Success() throws Exception {
        Long [] toBeDeleted = {1L,2L,3L};
        when(newsService.batchDelete(toBeDeleted)).thenReturn(new Long[]{1L});

        mockMvc.perform(delete("/api"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"{1}\""))
                .andDo(print());
    }

    @Test
    void deleteBatch_NewsWithIdNotExist_ThrowsException() throws Exception {
        when(newsService.getNewsById(ID)).thenThrow(new NullPointerException());

        mockMvc.perform(delete("/api/{id}/edit", ID))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}