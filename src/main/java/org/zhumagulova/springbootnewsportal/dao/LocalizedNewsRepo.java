package org.zhumagulova.springbootnewsportal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalizedNewsRepo extends JpaRepository<LocalizedNews, Long> {

    List<LocalizedNews> findAllByLanguageId(long languageId);

    Optional<LocalizedNews> findByNewsIdAndLanguageId(long newsId, long languageId);

    LocalizedNews save(LocalizedNews localizedNews);

    long deleteByNewsIdAndLanguageId(long id, long languageId);
}
