package org.zhumagulova.springbootnewsportal.service;


import org.zhumagulova.springbootnewsportal.entity.Language;

import java.util.Optional;

public interface LanguageService {

    long getLanguageIdByLocale();

    Optional<Language> getLanguageByLocale();
}
