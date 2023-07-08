package org.zhumagulova.springbootnewsportal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import org.zhumagulova.springbootnewsportal.dto.LocalizedNewsDto;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;
import org.zhumagulova.springbootnewsportal.model.NewsSource;

@Mapper (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@Component
public interface LocalizedNewsMapper {
    @Mapping(source = "localizedNews.newsSource.name", target = "newsSource")
    LocalizedNewsDto localizedNewsToDto(LocalizedNews localizedNews);

    LocalizedNews dtoToLocalizedNews (LocalizedNewsDto localizedNewsDto);

    void updateLocalizedNewsFromDto(LocalizedNewsDto dto, @MappingTarget LocalizedNews localizedNews);

    default NewsSource map(String value) {
        return new NewsSource(value);
    }
}
