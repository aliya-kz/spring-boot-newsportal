package org.zhumagulova.springbootnewsportal.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LanguageServiceImplTest extends BaseIntegrationTest {

    @Autowired
    private LanguageService languageService;

    @Sql(value = "classpath:test-sql/test-data.sql", executionPhase = Sql.ExecutionPhase
            .BEFORE_TEST_METHOD)
    @Test
    void getLanguageIdByLocale_LanguageExists_true() {
        assertNotNull(languageService.getLanguageByLocale().get());
    }

    @Test
    @Sql(value = "classpath:test-sql/delete-data.sql", executionPhase = Sql.ExecutionPhase
            .BEFORE_TEST_METHOD)
    void getLanguageIdByLocale_LanguageNotExist_emptyOptional() {
        assertTrue(languageService.getLanguageByLocale().isEmpty());
    }
}