package org.zhumagulova.springbootnewsportal.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class BaseIntegrationTest {
    private static final String IMAGE_VERSION = "postgres:13.2";

    public final static PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>(IMAGE_VERSION)
                .withDatabaseName("news_portal_test")
                .withUsername("postgres")
                .withPassword(System.getenv("POSTGRE_PASSWORD"))
                .withInitScript("test-sql/create-db.sql");
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("db.url", postgreSQLContainer::getJdbcUrl);
    }
}
