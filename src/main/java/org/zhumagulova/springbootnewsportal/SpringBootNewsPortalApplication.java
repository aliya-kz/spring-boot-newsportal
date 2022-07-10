package org.zhumagulova.springbootnewsportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "org.zhumagulova.springbootnewsportal.dao")
@SpringBootApplication (exclude = { SecurityAutoConfiguration.class})
public class SpringBootNewsPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootNewsPortalApplication.class, args);
    }



}
