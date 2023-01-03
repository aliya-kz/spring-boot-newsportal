package org.zhumagulova.springbootnewsportal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.zhumagulova.springbootnewsportal.dto.LocalizedNewsDto;
import org.zhumagulova.springbootnewsportal.model.LocalizedNews;

@Mapper (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LocalizedNewsMapper {
    LocalizedNewsMapper INSTANCE = Mappers.getMapper(LocalizedNewsMapper.class);
    LocalizedNewsDto localizedNewsToDto(LocalizedNews localizedNews);
    LocalizedNews dtoToLocalizedNews (LocalizedNewsDto localizedNewsDto);
    void updateLocalizedNewsFromDto(LocalizedNewsDto dto, @MappingTarget LocalizedNews localizedNews);
}
