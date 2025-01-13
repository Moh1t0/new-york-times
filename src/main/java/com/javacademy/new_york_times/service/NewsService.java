package com.javacademy.new_york_times.service;

import com.javacademy.new_york_times.dto.NewsDto;
import com.javacademy.new_york_times.dto.PageDto;
import com.javacademy.new_york_times.entity.NewsEntity;
import com.javacademy.new_york_times.mapper.NewsMapper;
import com.javacademy.new_york_times.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private static final int PAGE_SIZE = 10;

    public NewsDto save(NewsDto dto) {
        NewsEntity entity = newsMapper.toEntity(dto);
        newsRepository.save(entity);
        return newsMapper.toDto(entity);
    }

    /**
     * Переписать этот метод
     */
    public PageDto findAll(int page) {
        List<NewsDto> data = newsRepository.findAll().stream()
                .sorted(Comparator.comparing(NewsEntity::getNumber))
                .skip((long) PAGE_SIZE * page)
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
        NewsEntity existingEntity = newsRepository.findByNumber(dto.getNumber())
                .orElseThrow(() -> new RuntimeException("News with number %s - is not exists".formatted(dto.getNumber())));

        // Обновляем поля существующей сущности
        existingEntity.setTitle(dto.getTitle());
        existingEntity.setText(dto.getText());
        existingEntity.setAuthor(dto.getAuthor());

        // Сохраняем обновленную сущность
        newsRepository.update(existingEntity);
    }

    public String getNewsText(Integer newsNumber) {
        return newsRepository.findByNumber(newsNumber).map(NewsEntity::getText).orElseThrow();
    }

    public String getNewsAuthor(Integer newsNumber) {
        return newsRepository.findByNumber(newsNumber).map(NewsEntity::getAuthor).orElseThrow();
    }
}
