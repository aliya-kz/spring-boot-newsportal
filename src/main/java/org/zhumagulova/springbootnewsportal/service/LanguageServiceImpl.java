package org.zhumagulova.springbootnewsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhumagulova.springbootnewsportal.dao.LanguageRepo;
import org.zhumagulova.springbootnewsportal.models.Language;

import java.util.Locale;
import java.util.Optional;


@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepo languageRepo;

    @Autowired
    public LanguageServiceImpl(LanguageRepo languageRepo) {
        this.languageRepo = languageRepo;
    }

    @Transactional
    @Override
    public long getLanguageIdByLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        String langCode = locale.getLanguage();
        return languageRepo.findIdByCode(langCode);
    }


    @Transactional
    @Override
    public Optional<Language> getLanguageByLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        String langCode = locale.getLanguage();
        return languageRepo.findByCode(langCode);
    }
}
