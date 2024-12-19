package com.javacademy.new_york_times.mapper;

import com.javacademy.new_york_times.dto.NewsDto;
import com.javacademy.new_york_times.entity.NewsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewsMapper {

    NewsEntity toEntity(NewsDto newsDto);

    NewsDto toDto(NewsEntity entity);

    List<NewsDto> toDtos(List<NewsEntity> entities);
}
