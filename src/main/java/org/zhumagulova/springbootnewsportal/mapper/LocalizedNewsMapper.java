package org.zhumagulova.springbootnewsportal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import org.zhumagulova.springbootnewsportal.dto.LocalizedNewsDto;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;

@Mapper (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@Component
public interface LocalizedNewsMapper {
    LocalizedNewsDto localizedNewsToDto(LocalizedNews localizedNews);
    LocalizedNews dtoToLocalizedNews (LocalizedNewsDto localizedNewsDto);
    void updateLocalizedNewsFromDto(LocalizedNewsDto dto, @MappingTarget LocalizedNews localizedNews);
}
