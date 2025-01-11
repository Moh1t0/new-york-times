package com.javacademy.new_york_times.service;

import com.javacademy.new_york_times.dto.NewsDto;
import com.javacademy.new_york_times.dto.PageDto;
import com.javacademy.new_york_times.entity.NewsEntity;
import com.javacademy.new_york_times.mapper.NewsMapper;
import com.javacademy.new_york_times.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private static final int PAGE_SIZE = 10;

    public void save(NewsDto dto) {
        newsRepository.save(newsMapper.toEntity(dto));
    }

    /**
     * Переписать этот метод
     */
    public PageDto findAll(int page) {
        List<NewsDto> data = newsRepository.findAll().stream()
                .sorted(Comparator.comparing(NewsEntity::getNumber))
                .skip(PAGE_SIZE * page)
                .limit(PAGE_SIZE)
                .map(newsMapper::toDto)
                .toList();
        int total = newsRepository.findAll().size() / PAGE_SIZE;
        return new PageDto(data, total, page, PAGE_SIZE, data.size());
    }

    public NewsDto findByNumber(Integer number) {
        return newsMapper.toDto(newsRepository.findByNumber(number).orElseThrow());
    }

    public boolean deleteByNumber(Integer number) {
        return newsRepository.deleteByNumber(number);
    }

    public void update(NewsDto dto) {
        newsRepository.update(newsMapper.toEntity(dto));
    }

    public String getNewsText(Integer newsNumber) {
        return newsRepository.findByNumber(newsNumber).map(NewsEntity::getText).orElseThrow();
    }

    public String getNewsAuthor(Integer newsNumber) {
        return newsRepository.findByNumber(newsNumber).map(NewsEntity::getAuthor).orElseThrow();
    }
}
