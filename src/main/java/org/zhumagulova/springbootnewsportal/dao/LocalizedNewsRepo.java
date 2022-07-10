package org.zhumagulova.springbootnewsportal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zhumagulova.springbootnewsportal.models.LocalizedNews;

import java.util.List;
import java.util.Optional;


@Repository
public interface LocalizedNewsRepo extends JpaRepository<LocalizedNews, Long> {

    List<LocalizedNews> findAllByLanguage_Id(long languageId);

    Optional<LocalizedNews> findByNews_IdAndLanguage_Id(long newsId, long languageId);

    LocalizedNews save(LocalizedNews localizedNews);

    @Query("delete from LocalizedNews where news.id=:id and language.id=:languageId")
    void deleteByNews_IdAndLanguage_Id(@Param("id") long id, @Param ("languageId") long languageId);
}
