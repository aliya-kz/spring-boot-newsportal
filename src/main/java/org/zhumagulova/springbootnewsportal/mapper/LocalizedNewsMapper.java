package org.zhumagulova.springbootnewsportal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import org.zhumagulova.springbootnewsportal.api.model.LocalizedNewsRequest;
import org.zhumagulova.springbootnewsportal.api.model.LocalizedNewsResponse;
import org.zhumagulova.springbootnewsportal.entity.LocalizedNews;
import org.zhumagulova.springbootnewsportal.entity.NewsSource;

@Mapper (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@Component
public interface LocalizedNewsMapper {

    LocalizedNews requestToEntity (LocalizedNewsRequest request);

    @Mapping(source = "localizedNews.newsSource.name", target = "newsSource")
    LocalizedNewsResponse entityToResponse (LocalizedNews localizedNews);

    void updateLocalizedNewsFromDto(LocalizedNewsRequest localizedNewsRequest, @MappingTarget LocalizedNews localizedNews);
    default NewsSource map(String value) {
        return new NewsSource(value);
    }
}
