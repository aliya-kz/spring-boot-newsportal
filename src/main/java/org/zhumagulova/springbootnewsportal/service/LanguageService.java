package org.zhumagulova.springbootnewsportal.service;


import org.zhumagulova.springbootnewsportal.models.Language;

import java.util.Optional;

public interface LanguageService {

    long getLanguageIdByLocale();

    Optional<Language> getLanguageByLocale();
}
