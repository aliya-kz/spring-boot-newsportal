package org.zhumagulova.springbootnewsportal.dao;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocalizedNewsRepo extends JpaRepository<LocalizedNews, Long> {

    List<LocalizedNews> findAllByLanguageId(long languageId);

    Optional<LocalizedNews> findByNewsIdAndLanguageId(long newsId, long languageId);
    @CachePut(cacheNames = "news", key = "#localizedNews.id")
    LocalizedNews save(LocalizedNews localizedNews);
    @CacheEvict(cacheNames = "news", key = "#id", beforeInvocation = true)
    long deleteByNewsIdAndLanguageId(long id, long languageId);

    Optional <LocalizedNews> findByTitleAndDate(String title, LocalDate date);
}
