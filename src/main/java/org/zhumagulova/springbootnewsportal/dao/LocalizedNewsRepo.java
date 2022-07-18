package org.zhumagulova.springbootnewsportal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zhumagulova.springbootnewsportal.models.LocalizedNews;

import java.util.List;
import java.util.Optional;


@Repository
public interface LocalizedNewsRepo extends JpaRepository<LocalizedNews, Long> {

    List<LocalizedNews> findAllByLanguage_Id(long languageId);

    Optional<LocalizedNews> findByNews_IdAndLanguage_Id(long newsId, long languageId);

    LocalizedNews save(LocalizedNews localizedNews);

    void deleteByNews_IdAndLanguage_Id(long id, long languageId);
}
