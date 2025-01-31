package com.javacademy.new_york_times.controller;

import com.javacademy.new_york_times.dto.NewsDto;
import com.javacademy.new_york_times.dto.PageDto;
import com.javacademy.new_york_times.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;


/**
 * Сделать 7 операций внутри контроллера.
 * 1. Создание новости. Должно чистить кэш.
 * 2. Удаление новости по id. Должно чистить кэш.
 * 3. Получение новости по id. Должно быть закэшировано.
 * 4. Получение всех новостей (новости должны отдаваться порциями по 10 штук). Должно быть закэшировано.
 * 5. Обновление новости по id. Должно чистить кэш.
 * 6. Получение текста конкретной новости.
 * 7. Получение автора конкретной новости.
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private final NewsService service;
    private static final int PAGE_SIZE = 10;

    @PostMapping()
    @CacheEvict("news")
    @ResponseStatus(CREATED)
    public void createNews(@RequestBody NewsDto newsDto) {
        service.save(newsDto);
    }

    @DeleteMapping("/{id}")
    @CacheEvict("news")
    public boolean deleteNews(@PathVariable Integer id) {
        return service.deleteByNumber(id);
    }

    @GetMapping("/{id}")
    @Cacheable("news")
    public NewsDto getNewsByNumber(@PathVariable Integer id) {
        return service.findByNumber(id);
    }

    @GetMapping
    @Cacheable("news")
    public ResponseEntity<PageDto> getAllNews(@RequestParam Integer page) {
        PageDto all = service.findAll(page);
        return ResponseEntity.ok(all);
    }

    @PatchMapping("/{id}")
    @CacheEvict("news")
    public void updateNewsByNumber(@PathVariable Integer id, @RequestBody NewsDto newsDto) {
        newsDto.setNumber(id);
        service.update(newsDto);
    }

    @GetMapping("/{id}/text")
    public String getNewsText(@PathVariable Integer id) {
       return service.getNewsText(id);
    }

    @GetMapping("/{id}/author")
    public String getAuthorNews(@PathVariable Integer id) {
       return service.getNewsAuthor(id);
    }

















}