package com.javacademy.new_york_times.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
    private List<NewsDto> content;
    private Integer countPages;
    private Integer currentPage;
    private Integer maxPageSize;
    private Integer size;










}
