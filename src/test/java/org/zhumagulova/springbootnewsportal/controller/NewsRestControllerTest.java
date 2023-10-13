package org.zhumagulova.springbootnewsportal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;
import org.zhumagulova.springbootnewsportal.api.model.LocalizedNewsRequest;
import org.zhumagulova.springbootnewsportal.api.model.LocalizedNewsResponse;
import org.zhumagulova.springbootnewsportal.exception.NewsAlreadyExistsException;
import org.zhumagulova.springbootnewsportal.exception.NewsNotFoundException;
import org.zhumagulova.springbootnewsportal.mapper.LocalizedNewsMapper;
import org.zhumagulova.springbootnewsportal.entity.Language;
import org.zhumagulova.springbootnewsportal.entity.LocalizedNews;
import org.zhumagulova.springbootnewsportal.service.NewsService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "admin@admin.com", password = "12345", authorities = {"ADMIN"})
class NewsRestControllerTest {

    private final static long ID = 1;
    private final static long NON_EXISTING_NEWS_ID = 17;
    private final static int LOCALIZED_NEWS_OF_SINGLE_LANGUAGE_LIST_SIZE = 2;
    private final static String TITLE = "Test title";
    private final static String BRIEF = "Test brief";
    private final static String CONTENT = "Test content";

    @MockBean
    private NewsService newsService;

    private final LocalizedNewsMapper localizedNewsMapper;

    WebApplicationContext context;

    private MockMvc mockMvc;

    LocalizedNews mockNews = LocalizedNews.builder()
            .title(TITLE)
            .brief(BRIEF)
            .content(CONTENT)
            .date(LocalDate.now())
            .language(new Language(ID))
            .build();

    LocalizedNewsRequest mockRequest = LocalizedNewsRequest.builder()
            .title(TITLE)
            .brief(BRIEF)
            .content(CONTENT)
            .date(LocalDate.now())
            .build();

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String requestBody = ow.writeValueAsString(mockNews);

    NewsRestControllerTest(LocalizedNewsMapper localizedNewsMapper, WebApplicationContext context) throws JsonProcessingException {
        this.localizedNewsMapper = localizedNewsMapper;
        this.context = context;
    }

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
        List<LocalizedNews> allNews = new ArrayList<>(Arrays.asList(mockNews, new LocalizedNews()));

        when(newsService.getAllNews()).thenReturn(allNews);

        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(LOCALIZED_NEWS_OF_SINGLE_LANGUAGE_LIST_SIZE)))
                .andExpect(jsonPath("$[0].title").value(TITLE))
                .andDo(print());
    }

    @Test
    void showNewsById_success() throws Exception {
        when(newsService.getNewsById(ID)).thenReturn(Optional.of(mockNews));
        mockMvc.perform(get("/api/{id}", ID)).
                andExpect(status().isOk())
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
                .andExpect(content().string(containsString("Could not find news with id : " + ID)))
                .andDo(print());
    }

    @Test
    void createLocalizedNews_Success() throws Exception {

        when(newsService.createNews(mockNews, ID)).thenReturn(mockNews);

        LocalizedNewsResponse dto = localizedNewsMapper.entityToResponse(mockNews);

        mockMvc.perform(post("/api/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(mockNews.getTitle()))
                .andDo(print());
    }

    @Test
    void createLocalizedNews_NewsWithIdExist_ThrowsException() throws Exception {
        when(newsService.createNews(mockNews, ID)).thenThrow(new NewsAlreadyExistsException(ID));

        mockMvc.perform(post("/api/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.reason").value("News with id 1 already exist"))
                .andDo(print());
    }

    @Test
    void updateLocalizedNews_Success() throws Exception {
        when(newsService.updateNews(mockRequest, ID)).thenReturn(mockNews);

        mockMvc.perform(patch("/api/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(mockNews.getTitle()))
                .andDo(print());
    }

    @Test
    void updateLocalizedNews_NewsWithIdNotExist_ThrowsException() throws Exception {
        when(newsService.updateNews(mockRequest, ID)).thenThrow(new NullPointerException());

        mockMvc.perform(patch("/api/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.reason").value("No entity found"))
                .andDo(print());
    }

    @Test
    void deleteLocalizedNews_Success() throws Exception {
        doNothing().when(newsService).delete(ID);

        mockMvc.perform(delete("/api/{id}", ID))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteLocalizedNews_NewsWithIdNotExist_ThrowsException() throws Exception {
        doThrow(new NewsNotFoundException(ID)).when(newsService).delete(ID);

        mockMvc.perform(delete("/api/{id}", ID))
                .andExpect(jsonPath("$.reason").value("Could not find news with id : " + ID))
                .andDo(print());
    }

    @Test
    void deleteBatch_Success() throws Exception {
        long[] toBeDeleted = {ID};
        when(newsService.batchDelete(toBeDeleted)).thenReturn(new long[]{});

        mockMvc.perform(delete("/api")
                        .param("ids", String.valueOf(ID)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteBatch_NewsWithIdNotExist_ThrowsException() throws Exception {
        long[] toBeDeleted = {ID, NON_EXISTING_NEWS_ID};
        when(newsService.batchDelete(toBeDeleted)).thenReturn(new long[]{NON_EXISTING_NEWS_ID});

        mockMvc.perform(delete("/api", ID)
                        .param("ids", String.valueOf(ID))
                        .param("ids", String.valueOf(NON_EXISTING_NEWS_ID)))
                .andExpect(jsonPath("$.reason").value("Delete transaction rolled back. The following ids not found: "
                        + NON_EXISTING_NEWS_ID))
                .andDo(print());
    }

}