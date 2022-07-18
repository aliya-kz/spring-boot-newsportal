package org.zhumagulova.springbootnewsportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@EnableJpaRepositories(basePackages = "org.zhumagulova.springbootnewsportal.dao")
@SpringBootApplication
public class SpringBootNewsPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootNewsPortalApplication.class, args);
    }

    @Bean
    HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

}
