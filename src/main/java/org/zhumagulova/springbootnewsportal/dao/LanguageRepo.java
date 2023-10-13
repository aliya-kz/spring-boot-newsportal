package org.zhumagulova.springbootnewsportal.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.zhumagulova.springbootnewsportal.entity.Language;

import java.util.Optional;

@Repository
public interface LanguageRepo extends JpaRepository<Language, Long> {
    Optional<Language> findByCode(String langCode);

    @Query("select l.id from Language l where l.code = :code")
    long findIdByCode (String code);
}

